import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../services/api';
import './Auth.css';

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await register(form.username, form.email, form.password);
      navigate('/');
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Inscription impossible.');
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-card">
        <p className="auth-kicker">Commencez votre expérience</p>
        <h1>Créer un compte</h1>
        <p className="auth-intro">Enregistrez vos préférences alimentaires et construisez vos menus sur mesure.</p>
        <form className="auth-form" onSubmit={submit}>
          <label>
            Nom d'utilisateur
            <input value={form.username} onChange={(event) => setForm({ ...form, username: event.target.value })} required />
          </label>
          <label>
            Email
            <input type="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} required />
          </label>
          <label>
            Mot de passe
            <input type="password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} required />
          </label>
          <button type="submit">S'inscrire</button>
        </form>
        {error && <p role="alert">{error}</p>}
        <p className="auth-switch">Vous avez déjà un compte ? <a href="/login">Se connecter</a></p>
      </div>
    </section>
  );
}
