const { useState, useEffect, useCallback, useRef } = React;

// ─── API ──────────────────────────────────────────────────────────────
async function api(method, path, body) {
  const token = localStorage.getItem('token');
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  const res = await fetch(path, { method, headers, body: body ? JSON.stringify(body) : undefined });
  if (res.status === 401) { logout(); return; }
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || `Erro ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}
const GET  = path        => api('GET',    path);
const POST = (path, body)=> api('POST',   path, body);
const PUT  = (path, body)=> api('PUT',    path, body);
const DEL  = path        => api('DELETE', path);

// ─── AUTH ─────────────────────────────────────────────────────────────
function decodeJwt(token) {
  try { return JSON.parse(atob(token.split('.')[1])); } catch { return null; }
}
function getSession() {
  const token = localStorage.getItem('token');
  if (!token) return null;
  const payload = decodeJwt(token);
  if (!payload) return null;
  return { token, email: payload.sub, roles: payload.roles || [] };
}
function logout() {
  localStorage.removeItem('token');
  window.location.reload();
}

// ─── HOOKS ────────────────────────────────────────────────────────────
function useFetch(path, active = true) {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState('');
  const load = useCallback(async () => {
    if (!active || !path) { setLoading(false); return; }
    setLoading(true); setError('');
    try { setData(await GET(path)); }
    catch (e) { setError(e.message); }
    finally { setLoading(false); }
  }, [path, active]);
  useEffect(() => { load(); }, [load]);
  return { data, loading, error, reload: load };
}

// ─── COMPONENTES COMUNS ───────────────────────────────────────────────
function Spinner() { return <span className="spinner" />; }

function Alert({ type = 'error', msg }) {
  if (!msg) return null;
  return <div className={`alert alert-${type}`}>{msg}</div>;
}

function Modal({ title, onClose, children }) {
  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-title">{title}</div>
        {children}
      </div>
    </div>
  );
}

function ConfirmModal({ title, msg, onConfirm, onCancel }) {
  return (
    <Modal title={title} onClose={onCancel}>
      <p style={{ color: '#6b7280', fontSize: '.9rem' }}>{msg}</p>
      <div className="modal-actions">
        <button className="btn btn-ghost" onClick={onCancel}>Cancelar</button>
        <button className="btn btn-danger" onClick={onConfirm}>Confirmar</button>
      </div>
    </Modal>
  );
}

function EmptyState({ icon, text }) {
  return (
    <div className="empty-state">
      <div className="empty-icon">{icon}</div>
      <p>{text}</p>
    </div>
  );
}

// ─── AUTH PAGE ────────────────────────────────────────────────────────
function AuthPage({ onLogin }) {
  const [tab, setTab]     = useState('login');   // 'login' | 'register'
  const [role, setRole]   = useState('aluno');   // 'aluno' | 'colaborador'
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [nome, setNome]   = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  function resetForm() { setEmail(''); setSenha(''); setNome(''); setError(''); setSuccess(''); }

  function switchTab(t) { setTab(t); resetForm(); }
  function switchRole(r) { setRole(r); resetForm(); }

  async function handleLogin(e) {
    e.preventDefault();
    setLoading(true); setError('');
    try {
      const res = await POST('/v1/auth/login', { email, senha });
      localStorage.setItem('token', res.token);
      onLogin(getSession());
    } catch (err) {
      setError(err.message);
    } finally { setLoading(false); }
  }

  async function handleRegister(e) {
    e.preventDefault();
    setLoading(true); setError(''); setSuccess('');
    try {
      const endpoint = role === 'aluno' ? '/v1/auth/register/aluno' : '/v1/auth/register/funcionario';
      const body = role === 'aluno' ? { email, senha, nome } : { email, senha };
      await POST(endpoint, body);
      setSuccess(`Conta criada com sucesso! ${role === 'aluno' ? 'Já pode fazer login.' : 'Um administrador irá ativar seu acesso.'} Faça login.`);
      resetForm();
      setTimeout(() => switchTab('login'), 2500);
    } catch (err) {
      setError(err.message);
    } finally { setLoading(false); }
  }

  return (
    <div className="auth-bg">
      <div className="auth-card">
        <div className="auth-logo">
          <span className="icon">🏋️</span>
          <h1>Academy System</h1>
          <p>Gestão de Academia</p>
        </div>

        <div className="auth-tabs">
          <button className={`auth-tab ${tab === 'login' ? 'active' : ''}`} onClick={() => switchTab('login')}>Entrar</button>
          <button className={`auth-tab ${tab === 'register' ? 'active' : ''}`} onClick={() => switchTab('register')}>Cadastrar</button>
        </div>

        <div className="role-toggle">
          <button className={`role-btn ${role === 'aluno' ? 'active' : ''}`} onClick={() => switchRole('aluno')}>
            🎓 Aluno
          </button>
          <button className={`role-btn ${role === 'colaborador' ? 'active' : ''}`} onClick={() => switchRole('colaborador')}>
            👤 Colaborador
          </button>
        </div>

        <div className="auth-body">
          <Alert type="error"   msg={error} />
          <Alert type="success" msg={success} />

          {tab === 'login' ? (
            <form onSubmit={handleLogin}>
              <div className="form-group">
                <label>E-mail</label>
                <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="seu@email.com" required />
              </div>
              <div className="form-group">
                <label>Senha</label>
                <input type="password" value={senha} onChange={e => setSenha(e.target.value)} placeholder="••••••••••" required />
              </div>
              <button className="btn btn-primary btn-full" type="submit" disabled={loading}>
                {loading ? <><Spinner /> Entrando…</> : `Entrar como ${role === 'aluno' ? 'Aluno' : 'Colaborador'}`}
              </button>
            </form>
          ) : (
            <form onSubmit={handleRegister}>
              {role === 'aluno' && (
                <div className="form-group">
                  <label>Nome Completo</label>
                  <input type="text" value={nome} onChange={e => setNome(e.target.value)} placeholder="João da Silva" required />
                </div>
              )}
              <div className="form-group">
                <label>E-mail</label>
                <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="seu@email.com" required />
              </div>
              <div className="form-group">
                <label>Senha <span style={{ color: '#9ca3af', fontWeight: 400 }}>(mínimo 10 caracteres)</span></label>
                <input type="password" value={senha} onChange={e => setSenha(e.target.value)} placeholder="••••••••••" minLength={10} required />
              </div>
              <button className="btn btn-success btn-full" type="submit" disabled={loading}>
                {loading ? <><Spinner /> Cadastrando…</> : `Criar conta de ${role === 'aluno' ? 'Aluno' : 'Colaborador'}`}
              </button>
              <p style={{ marginTop: 12, fontSize: '.78rem', color: '#9ca3af', textAlign: 'center', lineHeight: 1.5 }}>
                {role === 'aluno'
                  ? 'Perfil criado automaticamente. Faça login após o cadastro.'
                  : 'Após o cadastro, um administrador ativará seu acesso.'}
              </p>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}

// ─── DASHBOARD ALUNO ──────────────────────────────────────────────────
function AlunoPage({ session }) {
  const { data: perfil, loading: perfilLoading } = useFetch('/v1/alunos/me');
  const alunoId = perfil?.id;

  const { data: avaliacao, loading: avaliacaoLoading, error: avaliacaoError } = useFetch(
    alunoId ? `/v1/alunos/${alunoId}/avaliacao` : null, !!alunoId
  );

  const { data: treinos, loading: treinosLoading, reload: reloadTreinos } = useFetch('/v1/treinos/meus');
  const { data: exercicios } = useFetch('/v1/exercicios');

  const [modalTreino, setModalTreino] = useState(false);
  const [formNome, setFormNome]       = useState('');
  const [formExercicios, setFormExercicios] = useState([]);
  const [formError, setFormError]     = useState('');
  const [formLoading, setFormLoading] = useState(false);

  function toggleExercicio(id) {
    setFormExercicios(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  }

  function openModalTreino() {
    setModalTreino(true); setFormNome(''); setFormExercicios([]); setFormError('');
  }

  async function handleCriarTreino(e) {
    e.preventDefault();
    if (formExercicios.length === 0) { setFormError('Selecione ao menos um exercício.'); return; }
    setFormLoading(true); setFormError('');
    try {
      await POST('/v1/treinos/meus', { nome: formNome, exerciciosIds: formExercicios });
      reloadTreinos();
      setModalTreino(false);
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg)', padding: '0' }}>
      <div className="topbar" style={{ marginBottom: 0 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <span style={{ fontSize: '1.3rem' }}>🏋️</span>
          <strong>Academy System</strong>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <span style={{ fontSize: '.82rem', color: '#6b7280' }}>🎓 {session.email}</span>
          <button className="btn btn-ghost btn-sm" onClick={logout}>Sair</button>
        </div>
      </div>

      <div className="aluno-page">
        <div className="aluno-header">
          <div>
            <h2>Meu Painel</h2>
            <p style={{ color: '#6b7280', fontSize: '.875rem', marginTop: 4 }}>
              {perfil ? `Bem-vindo(a), ${perfil.nome}!` : 'Bem-vindo(a) ao seu espaço na academia!'}
            </p>
          </div>
        </div>

        <div className="aluno-grid">
          {/* AVALIAÇÃO FÍSICA */}
          <div className="info-card" style={{ gridColumn: '1 / -1' }}>
            <div className="info-card-title">⚖️ Minha Avaliação Física</div>
            {perfilLoading ? (
              <div className="loading"><Spinner /> Carregando…</div>
            ) : !perfil ? (
              <EmptyState icon="⏳" text="Seu perfil ainda está sendo ativado por um colaborador. Assim que estiver pronto, suas informações aparecerão aqui." />
            ) : avaliacaoLoading ? (
              <div className="loading"><Spinner /> Carregando avaliação…</div>
            ) : avaliacaoError ? (
              <EmptyState icon="📋" text="Você ainda não possui avaliação física. Solicite ao seu colaborador para registrá-la." />
            ) : avaliacao ? (
              <div>
                <div className="info-row"><span className="label">Peso</span><span className="value">{avaliacao.peso} kg</span></div>
                <div className="info-row"><span className="label">Altura</span><span className="value">{avaliacao.altura} m</span></div>
                <div className="info-row"><span className="label">% Gordura Corporal</span><span className="value">{avaliacao.porcentagemGorduraCorporal}%</span></div>
                <div className="info-row"><span className="label">IMC</span><span className="value">{(avaliacao.peso / (avaliacao.altura * avaliacao.altura)).toFixed(1)}</span></div>
              </div>
            ) : null}
          </div>

          {/* TREINOS */}
          <div className="info-card" style={{ gridColumn: '1 / -1' }}>
            <div className="table-header" style={{ marginBottom: 12 }}>
              <div className="info-card-title" style={{ margin: 0 }}>📋 Meus Treinos</div>
              {perfil && <button className="btn btn-primary btn-sm" onClick={openModalTreino}>+ Novo Treino</button>}
            </div>
            {treinosLoading ? (
              <div className="loading"><Spinner /> Carregando treinos…</div>
            ) : !treinos || treinos.length === 0 ? (
              <EmptyState icon="💪" text="Você ainda não tem treinos. Crie seu primeiro treino ou aguarde o colaborador atribuir um." />
            ) : (
              <table>
                <thead><tr><th>Nome</th><th>Exercícios</th></tr></thead>
                <tbody>
                  {treinos.map(t => (
                    <tr key={t.id}>
                      <td><strong>{t.nome}</strong></td>
                      <td>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
                          {t.exercicios?.map(ex => (
                            <span key={ex.id} className="badge badge-blue">{ex.nome}</span>
                          ))}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {modalTreino && (
        <Modal title="Criar Novo Treino" onClose={() => setModalTreino(false)}>
          <Alert msg={formError} />
          <form onSubmit={handleCriarTreino}>
            <div className="form-group">
              <label>Nome do Treino</label>
              <input type="text" value={formNome} onChange={e => setFormNome(e.target.value)} placeholder="Ex: Treino A — Peito e Tríceps" required />
            </div>
            <div className="form-group">
              <label>Exercícios <span style={{ color: '#9ca3af', fontWeight: 400 }}>(selecione um ou mais)</span></label>
              <div className="multi-options-box">
                {!exercicios || exercicios.length === 0
                  ? <p style={{ padding: 8, color: '#9ca3af', fontSize: '.82rem' }}>Nenhum exercício disponível.</p>
                  : exercicios.map(ex => (
                    <label key={ex.id} className="multi-option">
                      <input type="checkbox" checked={formExercicios.includes(ex.id)} onChange={() => toggleExercicio(ex.id)} />
                      <span>{ex.nome}</span>
                      <span className="badge badge-blue" style={{ marginLeft: 'auto' }}>{ex.grupoMuscular}</span>
                    </label>
                  ))
                }
              </div>
              {formExercicios.length > 0 && <p className="form-hint">{formExercicios.length} exercício(s) selecionado(s)</p>}
            </div>
            <div className="modal-actions">
              <button type="button" className="btn btn-ghost" onClick={() => setModalTreino(false)}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={formLoading}>
                {formLoading ? <><Spinner />Salvando…</> : 'Criar Treino'}
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}

// ─── COLABORADOR — SEÇÕES ─────────────────────────────────────────────

// ── ALUNOS ────────────────────────────────
function SecaoAlunos() {
  const { data: alunos, loading, error, reload } = useFetch('/v1/alunos');
  const [modal, setModal] = useState(false);
  const [deleting, setDeleting] = useState(null);
  // form state - passo 1: criar user, passo 2: criar perfil
  const [step, setStep]           = useState(1);
  const [userId, setUserId]       = useState('');
  const [formEmail, setFormEmail] = useState('');
  const [formSenha, setFormSenha] = useState('');
  const [formNome, setFormNome]   = useState('');
  const [formError, setFormError] = useState('');
  const [formLoading, setFormLoading] = useState(false);

  function openModal() { setModal(true); setStep(1); setUserId(''); setFormEmail(''); setFormSenha(''); setFormNome(''); setFormError(''); }
  function closeModal() { setModal(false); }

  async function handleStep1(e) {
    e.preventDefault();
    setFormLoading(true); setFormError('');
    try {
      const res = await POST('/v1/auth/register/aluno', { email: formEmail, senha: formSenha });
      setUserId(res.id);
      setStep(2);
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  async function handleStep2(e) {
    e.preventDefault();
    setFormLoading(true); setFormError('');
    try {
      await POST('/v1/alunos', { usuarioId: userId, nome: formNome });
      reload();
      closeModal();
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  async function handleDelete(id) {
    try { await DEL(`/v1/alunos/${id}`); reload(); }
    catch (err) { alert(err.message); }
    setDeleting(null);
  }

  return (
    <div>
      <div className="table-card">
        <div className="table-header">
          <h4>Alunos {alunos && <span style={{ color: '#9ca3af', fontWeight: 400, fontSize: '.8rem' }}>({alunos.length})</span>}</h4>
          <button className="btn btn-primary btn-sm" onClick={openModal}>+ Novo Aluno</button>
        </div>
        {loading && <div className="loading"><Spinner /> Carregando…</div>}
        {error   && <div style={{ padding: 16 }}><Alert msg={error} /></div>}
        {!loading && !error && (
          alunos?.length === 0
            ? <EmptyState icon="🎓" text="Nenhum aluno cadastrado ainda." />
            : <table>
                <thead><tr><th>Nome</th><th>E-mail</th><th>ID</th><th></th></tr></thead>
                <tbody>
                  {alunos.map(a => (
                    <tr key={a.id}>
                      <td><strong>{a.nome}</strong></td>
                      <td>{a.email}</td>
                      <td style={{ fontSize: '.72rem', color: '#9ca3af' }}>{a.id?.substring(0,8)}…</td>
                      <td><button className="btn btn-danger btn-sm" onClick={() => setDeleting(a)}>Excluir</button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
        )}
      </div>

      {modal && (
        <Modal title="Cadastrar Novo Aluno" onClose={closeModal}>
          <div className="steps">
            <div className={`step ${step === 1 ? 'active' : step > 1 ? 'done' : ''}`}>1. Criar Conta</div>
            <div className={`step ${step === 2 ? 'active' : ''}`}>2. Dados do Perfil</div>
          </div>
          <Alert msg={formError} />
          {step === 1 ? (
            <form onSubmit={handleStep1}>
              <div className="form-group">
                <label>E-mail</label>
                <input type="email" value={formEmail} onChange={e => setFormEmail(e.target.value)} placeholder="aluno@email.com" required />
              </div>
              <div className="form-group">
                <label>Senha <span style={{ color: '#9ca3af', fontWeight: 400 }}>(mín. 10 caracteres)</span></label>
                <input type="password" value={formSenha} onChange={e => setFormSenha(e.target.value)} minLength={10} required />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-ghost" onClick={closeModal}>Cancelar</button>
                <button type="submit" className="btn btn-primary" disabled={formLoading}>{formLoading ? <><Spinner />Criando…</> : 'Próximo'}</button>
              </div>
            </form>
          ) : (
            <form onSubmit={handleStep2}>
              <div className="form-group">
                <label>Nome Completo</label>
                <input type="text" value={formNome} onChange={e => setFormNome(e.target.value)} placeholder="João da Silva" required />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-ghost" onClick={closeModal}>Cancelar</button>
                <button type="submit" className="btn btn-success" disabled={formLoading}>{formLoading ? <><Spinner />Salvando…</> : 'Cadastrar Aluno'}</button>
              </div>
            </form>
          )}
        </Modal>
      )}

      {deleting && (
        <ConfirmModal
          title="Excluir Aluno"
          msg={`Confirma a exclusão de "${deleting.nome}"? Esta ação não pode ser desfeita.`}
          onConfirm={() => handleDelete(deleting.id)}
          onCancel={() => setDeleting(null)}
        />
      )}
    </div>
  );
}

// ── EXERCÍCIOS ────────────────────────────
function SecaoExercicios() {
  const { data: exercicios, loading, error, reload } = useFetch('/v1/exercicios');
  const [modal, setModal]         = useState(false);
  const [deleting, setDeleting]   = useState(null);
  const [formNome, setFormNome]   = useState('');
  const [formGrupo, setFormGrupo] = useState('');
  const [formError, setFormError] = useState('');
  const [formLoading, setFormLoading] = useState(false);

  function openModal() { setModal(true); setFormNome(''); setFormGrupo(''); setFormError(''); }

  async function handleSave(e) {
    e.preventDefault();
    setFormLoading(true); setFormError('');
    try {
      await POST('/v1/exercicios', { nome: formNome, grupoMuscular: formGrupo });
      reload(); setModal(false);
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  async function handleDelete(id) {
    try { await DEL(`/v1/exercicios/${id}`); reload(); }
    catch (err) { alert(err.message); }
    setDeleting(null);
  }

  const GRUPOS = ['Peito', 'Costas', 'Ombro', 'Bíceps', 'Tríceps', 'Pernas', 'Glúteo', 'Abdômen', 'Panturrilha'];

  return (
    <div>
      <div className="table-card">
        <div className="table-header">
          <h4>Exercícios {exercicios && <span style={{ color: '#9ca3af', fontWeight: 400, fontSize: '.8rem' }}>({exercicios.length})</span>}</h4>
          <button className="btn btn-primary btn-sm" onClick={openModal}>+ Novo Exercício</button>
        </div>
        {loading && <div className="loading"><Spinner /> Carregando…</div>}
        {error   && <div style={{ padding: 16 }}><Alert msg={error} /></div>}
        {!loading && !error && (
          exercicios?.length === 0
            ? <EmptyState icon="💪" text="Nenhum exercício cadastrado ainda." />
            : <table>
                <thead><tr><th>Nome</th><th>Grupo Muscular</th><th></th></tr></thead>
                <tbody>
                  {exercicios.map(ex => (
                    <tr key={ex.id}>
                      <td><strong>{ex.nome}</strong></td>
                      <td><span className="badge badge-blue">{ex.grupoMuscular}</span></td>
                      <td><button className="btn btn-danger btn-sm" onClick={() => setDeleting(ex)}>Excluir</button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
        )}
      </div>

      {modal && (
        <Modal title="Novo Exercício" onClose={() => setModal(false)}>
          <Alert msg={formError} />
          <form onSubmit={handleSave}>
            <div className="form-group">
              <label>Nome do Exercício</label>
              <input type="text" value={formNome} onChange={e => setFormNome(e.target.value)} placeholder="Ex: Supino Reto" required />
            </div>
            <div className="form-group">
              <label>Grupo Muscular</label>
              <select value={formGrupo} onChange={e => setFormGrupo(e.target.value)} required>
                <option value="">Selecione…</option>
                {GRUPOS.map(g => <option key={g} value={g}>{g}</option>)}
              </select>
            </div>
            <div className="modal-actions">
              <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={formLoading}>{formLoading ? <><Spinner />Salvando…</> : 'Salvar'}</button>
            </div>
          </form>
        </Modal>
      )}

      {deleting && (
        <ConfirmModal
          title="Excluir Exercício"
          msg={`Confirma a exclusão de "${deleting.nome}"?`}
          onConfirm={() => handleDelete(deleting.id)}
          onCancel={() => setDeleting(null)}
        />
      )}
    </div>
  );
}

// ── AVALIAÇÕES FÍSICAS ────────────────────
function SecaoAvaliacoes() {
  const { data: avaliacoes, loading, error, reload } = useFetch('/v1/avaliacoesfisicas');
  const { data: alunos }   = useFetch('/v1/alunos');
  const [modal, setModal]  = useState(false);
  const [deleting, setDeleting] = useState(null);
  const [form, setForm]    = useState({ alunoId: '', peso: '', altura: '', porcentagemGorduraCorporal: '' });
  const [formError, setFormError]   = useState('');
  const [formLoading, setFormLoading] = useState(false);

  function openModal() { setModal(true); setForm({ alunoId: '', peso: '', altura: '', porcentagemGorduraCorporal: '' }); setFormError(''); }

  async function handleSave(e) {
    e.preventDefault();
    setFormLoading(true); setFormError('');
    try {
      await POST('/v1/avaliacoesfisicas', { alunoId: form.alunoId, peso: parseFloat(form.peso), altura: parseFloat(form.altura), porcentagemGorduraCorporal: parseFloat(form.porcentagemGorduraCorporal) });
      reload(); setModal(false);
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  async function handleDelete(id) {
    try { await DEL(`/v1/avaliacoesfisicas/${id}`); reload(); }
    catch (err) { alert(err.message); }
    setDeleting(null);
  }

  return (
    <div>
      <div className="table-card">
        <div className="table-header">
          <h4>Avaliações Físicas</h4>
          <button className="btn btn-primary btn-sm" onClick={openModal}>+ Nova Avaliação</button>
        </div>
        {loading && <div className="loading"><Spinner /> Carregando…</div>}
        {error   && <div style={{ padding: 16 }}><Alert msg={error} /></div>}
        {!loading && !error && (
          avaliacoes?.length === 0
            ? <EmptyState icon="⚖️" text="Nenhuma avaliação física registrada." />
            : <table>
                <thead><tr><th>Aluno</th><th>Peso</th><th>Altura</th><th>% Gordura</th><th>IMC</th><th></th></tr></thead>
                <tbody>
                  {avaliacoes.map(av => {
                    const imc = av.peso && av.altura ? (av.peso / (av.altura * av.altura)).toFixed(1) : '—';
                    return (
                      <tr key={av.idAvaliacao}>
                        <td><strong>{av.nomeAluno}</strong></td>
                        <td>{av.peso} kg</td>
                        <td>{av.altura} m</td>
                        <td><span className="badge badge-orange">{av.porcentagemGorduraCorporal}%</span></td>
                        <td>{imc}</td>
                        <td><button className="btn btn-danger btn-sm" onClick={() => setDeleting(av)}>Excluir</button></td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
        )}
      </div>

      {modal && (
        <Modal title="Nova Avaliação Física" onClose={() => setModal(false)}>
          <Alert msg={formError} />
          <form onSubmit={handleSave}>
            <div className="form-group">
              <label>Aluno</label>
              <select value={form.alunoId} onChange={e => setForm({ ...form, alunoId: e.target.value })} required>
                <option value="">Selecione o aluno…</option>
                {alunos?.map(a => <option key={a.id} value={a.id}>{a.nome}</option>)}
              </select>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
              <div className="form-group">
                <label>Peso (kg)</label>
                <input type="number" step="0.1" value={form.peso} onChange={e => setForm({ ...form, peso: e.target.value })} placeholder="80.5" required />
              </div>
              <div className="form-group">
                <label>Altura (m)</label>
                <input type="number" step="0.01" value={form.altura} onChange={e => setForm({ ...form, altura: e.target.value })} placeholder="1.75" required />
              </div>
            </div>
            <div className="form-group">
              <label>% Gordura Corporal</label>
              <input type="number" step="0.1" value={form.porcentagemGorduraCorporal} onChange={e => setForm({ ...form, porcentagemGorduraCorporal: e.target.value })} placeholder="18.5" required />
            </div>
            <div className="modal-actions">
              <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={formLoading}>{formLoading ? <><Spinner />Salvando…</> : 'Registrar Avaliação'}</button>
            </div>
          </form>
        </Modal>
      )}

      {deleting && (
        <ConfirmModal
          title="Excluir Avaliação"
          msg={`Confirma a exclusão da avaliação de "${deleting.nomeAluno}"?`}
          onConfirm={() => handleDelete(deleting.idAvaliacao)}
          onCancel={() => setDeleting(null)}
        />
      )}
    </div>
  );
}

// ── TREINOS ───────────────────────────────
function SecaoTreinos() {
  const { data: treinos,   loading, error, reload } = useFetch('/v1/treinos');
  const { data: alunos }    = useFetch('/v1/alunos');
  const { data: exercicios} = useFetch('/v1/exercicios');
  const [modal, setModal]  = useState(false);
  const [deleting, setDeleting]   = useState(null);
  const [form, setForm]    = useState({ alunoId: '', nome: '', exerciciosIds: [] });
  const [formError, setFormError]   = useState('');
  const [formLoading, setFormLoading] = useState(false);

  function openModal() { setModal(true); setForm({ alunoId: '', nome: '', exerciciosIds: [] }); setFormError(''); }

  function toggleExercicio(id) {
    setForm(f => ({
      ...f,
      exerciciosIds: f.exerciciosIds.includes(id)
        ? f.exerciciosIds.filter(x => x !== id)
        : [...f.exerciciosIds, id]
    }));
  }

  async function handleSave(e) {
    e.preventDefault();
    if (form.exerciciosIds.length === 0) { setFormError('Selecione ao menos um exercício.'); return; }
    setFormLoading(true); setFormError('');
    try {
      await POST('/v1/treinos', { alunoId: form.alunoId, nome: form.nome, exerciciosIds: form.exerciciosIds });
      reload(); setModal(false);
    } catch (err) { setFormError(err.message); }
    finally { setFormLoading(false); }
  }

  async function handleDelete(id) {
    try { await DEL(`/v1/treinos/${id}`); reload(); }
    catch (err) { alert(err.message); }
    setDeleting(null);
  }

  const nomeAluno = (id) => alunos?.find(a => a.id === id)?.nome || id?.substring(0,8) + '…';

  return (
    <div>
      <div className="table-card">
        <div className="table-header">
          <h4>Treinos {treinos && <span style={{ color: '#9ca3af', fontWeight: 400, fontSize: '.8rem' }}>({treinos.length})</span>}</h4>
          <button className="btn btn-primary btn-sm" onClick={openModal}>+ Novo Treino</button>
        </div>
        {loading && <div className="loading"><Spinner /> Carregando…</div>}
        {error   && <div style={{ padding: 16 }}><Alert msg={error} /></div>}
        {!loading && !error && (
          treinos?.length === 0
            ? <EmptyState icon="📋" text="Nenhum treino cadastrado ainda." />
            : <table>
                <thead><tr><th>Nome</th><th>Aluno</th><th>Exercícios</th><th></th></tr></thead>
                <tbody>
                  {treinos.map(t => (
                    <tr key={t.id}>
                      <td><strong>{t.nome}</strong></td>
                      <td>{nomeAluno(t.alunoId)}</td>
                      <td><span className="badge badge-blue">{t.exercicios?.length || 0} exercício(s)</span></td>
                      <td><button className="btn btn-danger btn-sm" onClick={() => setDeleting(t)}>Excluir</button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
        )}
      </div>

      {modal && (
        <Modal title="Novo Treino" onClose={() => setModal(false)}>
          <Alert msg={formError} />
          <form onSubmit={handleSave}>
            <div className="form-group">
              <label>Aluno</label>
              <select value={form.alunoId} onChange={e => setForm({ ...form, alunoId: e.target.value })} required>
                <option value="">Selecione o aluno…</option>
                {alunos?.map(a => <option key={a.id} value={a.id}>{a.nome}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Nome do Treino</label>
              <input type="text" value={form.nome} onChange={e => setForm({ ...form, nome: e.target.value })} placeholder="Ex: Treino A — Peito e Tríceps" required />
            </div>
            <div className="form-group">
              <label>Exercícios <span style={{ color: '#9ca3af', fontWeight: 400 }}>(selecione um ou mais)</span></label>
              <div className="multi-options-box">
                {exercicios?.length === 0 && <p style={{ padding: 8, color: '#9ca3af', fontSize: '.82rem' }}>Nenhum exercício cadastrado.</p>}
                {exercicios?.map(ex => (
                  <label key={ex.id} className="multi-option">
                    <input type="checkbox" checked={form.exerciciosIds.includes(ex.id)} onChange={() => toggleExercicio(ex.id)} />
                    <span>{ex.nome}</span>
                    <span className="badge badge-blue" style={{ marginLeft: 'auto' }}>{ex.grupoMuscular}</span>
                  </label>
                ))}
              </div>
              {form.exerciciosIds.length > 0 && (
                <p className="form-hint">{form.exerciciosIds.length} exercício(s) selecionado(s)</p>
              )}
            </div>
            <div className="modal-actions">
              <button type="button" className="btn btn-ghost" onClick={() => setModal(false)}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={formLoading}>{formLoading ? <><Spinner />Salvando…</> : 'Criar Treino'}</button>
            </div>
          </form>
        </Modal>
      )}

      {deleting && (
        <ConfirmModal
          title="Excluir Treino"
          msg={`Confirma a exclusão de "${deleting.nome}"?`}
          onConfirm={() => handleDelete(deleting.id)}
          onCancel={() => setDeleting(null)}
        />
      )}
    </div>
  );
}

// ── DASHBOARD OVERVIEW ─────────────────────
function DashboardOverview() {
  const { data: alunos }     = useFetch('/v1/alunos');
  const { data: exercicios } = useFetch('/v1/exercicios');
  const { data: treinos }    = useFetch('/v1/treinos');
  const { data: avaliacoes } = useFetch('/v1/avaliacoesfisicas');

  const cnt = d => d === null ? '…' : d.length;

  return (
    <div>
      <div className="stats-grid">
        <div className="stat-card g">
          <div className="stat-icon">🎓</div>
          <div className="stat-label">Alunos</div>
          <div className="stat-value">{cnt(alunos)}</div>
        </div>
        <div className="stat-card p">
          <div className="stat-icon">💪</div>
          <div className="stat-label">Exercícios</div>
          <div className="stat-value">{cnt(exercicios)}</div>
        </div>
        <div className="stat-card o">
          <div className="stat-icon">📋</div>
          <div className="stat-label">Treinos</div>
          <div className="stat-value">{cnt(treinos)}</div>
        </div>
        <div className="stat-card r">
          <div className="stat-icon">⚖️</div>
          <div className="stat-label">Avaliações</div>
          <div className="stat-value">{cnt(avaliacoes)}</div>
        </div>
      </div>

      <div className="table-card">
        <div className="table-header"><h4>Alunos recentes</h4></div>
        {!alunos ? (
          <div className="loading"><Spinner /> Carregando…</div>
        ) : alunos.length === 0 ? (
          <EmptyState icon="🎓" text="Nenhum aluno cadastrado." />
        ) : (
          <table>
            <thead><tr><th>Nome</th><th>E-mail</th></tr></thead>
            <tbody>
              {alunos.slice(0, 5).map(a => (
                <tr key={a.id}><td><strong>{a.nome}</strong></td><td>{a.email}</td></tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

// ─── COLABORADOR PAGE ─────────────────────────────────────────────────
const MENU = [
  { key: 'dashboard',  icon: '📊', label: 'Dashboard' },
  { key: 'alunos',     icon: '🎓', label: 'Alunos' },
  { key: 'exercicios', icon: '💪', label: 'Exercícios' },
  { key: 'avaliacoes', icon: '⚖️',  label: 'Avaliações Físicas' },
  { key: 'treinos',    icon: '📋', label: 'Treinos' },
];
const TITLES = { dashboard: 'Dashboard', alunos: 'Alunos', exercicios: 'Exercícios', avaliacoes: 'Avaliações Físicas', treinos: 'Treinos' };

function ColaboradorPage({ session }) {
  const [page, setPage] = useState('dashboard');

  const Content = {
    dashboard:  DashboardOverview,
    alunos:     SecaoAlunos,
    exercicios: SecaoExercicios,
    avaliacoes: SecaoAvaliacoes,
    treinos:    SecaoTreinos,
  }[page];

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="sidebar-logo">
          <h2>🏋️ Academy</h2>
          <p>Painel do Colaborador</p>
        </div>
        <nav className="sidebar-nav">
          <div className="nav-section-label">Menu</div>
          {MENU.map(m => (
            <button key={m.key} className={`nav-item ${page === m.key ? 'active' : ''}`} onClick={() => setPage(m.key)}>
              <span className="nav-icon">{m.icon}</span>
              {m.label}
            </button>
          ))}
        </nav>
        <div className="sidebar-footer">
          <div className="sidebar-user">
            Conectado como<strong>{session.email}</strong>
          </div>
          <button className="btn btn-danger btn-sm btn-full" onClick={logout}>Sair</button>
        </div>
      </aside>

      <div className="main-area">
        <div className="topbar">
          <h3>{TITLES[page]}</h3>
        </div>
        <div className="page-content">
          <Content />
        </div>
      </div>
    </div>
  );
}

// ─── APP ROOT ─────────────────────────────────────────────────────────
function App() {
  const [session, setSession] = useState(() => getSession());

  if (!session) {
    return <AuthPage onLogin={s => setSession(s)} />;
  }

  const isAluno = session.roles.some(r => r === 'ROLE_ALUNO');
  if (isAluno) return <AlunoPage session={session} />;

  return <ColaboradorPage session={session} />;
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
