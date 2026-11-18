import {
  Link,
  useLocation,
  useNavigate,
} from 'react-router-dom';

import {
  clearToken,
  getRole,
  getToken,
} from '../services/api';

import './Navbar.css';

export default function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();

  // Vérifie si l'utilisateur possède actuellement un token.
  const isAuthenticated = Boolean(getToken());
  const isAdmin = getRole() === 'ROLE_ADMIN';

  const handleAuthClick = () => {
    // Si l'utilisateur n'est pas connecté,
    // le bouton redirige vers la page de connexion.
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    // Suppression du token local.
    clearToken();

    // Notification visible de déconnexion.
    window.dispatchEvent(
      new CustomEvent('regimenu:toast', {
        detail: {
          type: 'success',
          message: 'Vous avez été déconnecté avec succès.',
        },
      }),
    );

    // Redirection vers la page de connexion
    // avec un message explicatif.
    navigate('/login', {
      state: {
        loggedOut: true,
      },
    });
  };

  return (
    <header className="navbar-container">
      {/* Logo et nom de l'application */}
      <Link to="/" className="navbar-brand">
        <img
          className="navbar-logo"
          src="/favicon.svg"
          alt="Logo RegiMenu"
        />
        <span>RegiMenu</span>
      </Link>

      {/* Navigation principale */}
      <nav
        className="nav-links"
        aria-label="Navigation principale"
      >
        <Link
          to="/"
          className={`nav-item ${
            location.pathname === '/' ? 'active' : ''
          }`}
        >
          Accueil
        </Link>

        <Link
          to="/favoris"
          className={`nav-item ${
            location.pathname === '/favoris' ? 'active' : ''
          }`}
        >
          Favoris
        </Link>

        <Link
          to="/historique"
          className={`nav-item ${
            location.pathname === '/historique' ? 'active' : ''
          }`}
        >
          Historique
        </Link>

        {/* Ces liens sont visibles uniquement connecté */}
        {isAuthenticated && (
          <Link
            to="/profil"
            className={`nav-item ${
              location.pathname === '/profil' ? 'active' : ''
            }`}
          >
            Profil
          </Link>
        )}

        {isAdmin && (
          <Link
            to="/admin"
            className={`nav-item ${
              location.pathname === '/admin' ? 'active' : ''
            }`}
          >
            Administration
          </Link>
        )}

      </nav>

      {/* Bouton connexion / déconnexion */}
      <button
        className={`login-btn ${
          isAuthenticated ? 'logout-btn' : ''
        }`}
        onClick={handleAuthClick}
      >
        <span>{isAuthenticated ? '↪' : '→'}</span>

        {isAuthenticated ? 'Déconnexion' : 'Connexion'}
      </button>
    </header>
  );
}