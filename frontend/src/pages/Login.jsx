import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';
import './Auth.css';

export default function Login() {
  const navigate = useNavigate();
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(identifier, password);
      navigate('/');
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Identifiants invalides.');
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-card">
        <p className="auth-kicker">Bienvenue sur RegiMenu</p>
        <h1>Connexion</h1>
        <p className="auth-intro">Connectez-vous pour retrouver votre profil, vos favoris et vos menus personnalisés.</p>
        <form className="auth-form" onSubmit={submit}>
          <label>
            Email ou nom d'utilisateur
            <input value={identifier} onChange={(event) => setIdentifier(event.target.value)} required />
          </label>
          <label>
            Mot de passe
            <input type="password" value={password} onChange={(event) => setPassword(event.target.value)} required />
          </label>
          <button type="submit">Se connecter</button>
        </form>
        {error && <p role="alert">{error}</p>}
        <p className="auth-switch">Pas encore de compte ? <a href="/register">Créer un compte</a></p>
      </div>
    </section>
  );
}
