import { useState } from 'react';
import {
  Link,
  useNavigate,
} from 'react-router-dom';

import { register } from '../services/api';

import './Auth.css';

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));

    setError('');
  };

  const submit = async (event) => {
    event.preventDefault();

    setError('');
    setLoading(true);

    try {
      await register(
        form.username,
        form.email,
        form.password,
      );

      window.dispatchEvent(
        new CustomEvent('regimenu:toast', {
          detail: {
            type: 'success',
            message: 'Votre compte a été créé avec succès.',
          },
        }),
      );

      navigate('/');
    } catch (requestError) {
      const message =
        requestError.response?.data?.message ||
        'Inscription impossible. Vérifiez les informations saisies.';

      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-card">
        <p className="auth-kicker">
          Commencez votre expérience
        </p>

        <h1>Créer un compte</h1>

        <p className="auth-intro">
          Enregistrez vos préférences alimentaires
          et construisez vos menus sur mesure.
        </p>

        <form
          className="auth-form"
          onSubmit={submit}
        >
          <label>
            Nom d'utilisateur

            <input
              name="username"
              value={form.username}
              onChange={handleChange}
              autoComplete="username"
              required
            />
          </label>

          <label>
            Email

            <input
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              autoComplete="email"
              required
            />
          </label>

          <label>
            Mot de passe

            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              autoComplete="new-password"
              minLength="6"
              required
            />
          </label>

          <button
            type="submit"
            disabled={loading}
          >
            {loading ? 'Création...' : "S'inscrire"}
          </button>
        </form>

        {/* Erreur d'inscription */}
        {error && (
          <p role="alert">
            {error}
          </p>
        )}

        <p className="auth-switch">
          Vous avez déjà un compte ?{' '}
          <Link to="/login">
            Se connecter
          </Link>
        </p>
      </div>
    </section>
  );
}