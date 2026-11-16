import { useState } from 'react';
import {
  Link,
  useLocation,
  useNavigate,
} from 'react-router-dom';

import { login } from '../services/api';

import './Auth.css';

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();

  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Message affiché après une déconnexion ou une expiration.
  const sessionMessage = location.state?.loggedOut
    ? 'Vous avez été déconnecté. À bientôt !'
    : location.state?.expired
      ? 'Votre session a expiré. Veuillez vous reconnecter.'
      : '';

  const submit = async (event) => {
    event.preventDefault();

    setError('');
    setLoading(true);

    try {
      await login(identifier, password);

      // Notification de connexion réussie.
      window.dispatchEvent(
        new CustomEvent('regimenu:toast', {
          detail: {
            type: 'success',
            message: 'Connexion réussie. Bienvenue sur RegiMenu !',
          },
        }),
      );

      navigate('/');
    } catch (requestError) {
      const message =
        requestError.response?.data?.message ||
        'Identifiants invalides. Vérifiez vos informations.';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-card">
        <p className="auth-kicker">
          Bienvenue sur RegiMenu
        </p>

        <h1>Connexion</h1>

        <p className="auth-intro">
          Connectez-vous pour retrouver votre profil,
          vos favoris et vos menus personnalisés.
        </p>

        {/* Message après déconnexion ou expiration */}
        {sessionMessage && (
          <div className="auth-notice" role="status">
            <span>✓</span>
            {sessionMessage}
          </div>
        )}

        <form
          className="auth-form"
          onSubmit={submit}
        >
          <label>
            Email ou nom d'utilisateur

            <input
              value={identifier}
              onChange={(event) => {
                setIdentifier(event.target.value);
                setError('');
              }}
              autoComplete="username"
              required
            />
          </label>

          <label>
            Mot de passe

            <input
              type="password"
              value={password}
              onChange={(event) => {
                setPassword(event.target.value);
                setError('');
              }}
              autoComplete="current-password"
              required
            />
          </label>

          <button
            type="submit"
            disabled={loading}
          >
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>

        {/* Erreur de connexion */}
        {error && (
          <p role="alert">
            {error}
          </p>
        )}

        <p className="auth-switch">
          Pas encore de compte ?{' '}
          <Link to="/register">
            Créer un compte
          </Link>
        </p>
      </div>
    </section>
  );
}