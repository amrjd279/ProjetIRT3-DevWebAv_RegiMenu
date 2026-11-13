import {
  Link,
  useLocation
} from 'react-router-dom';
import { clearToken, getToken } from '../services/api';

import './Navbar.css';


export default function Navbar() {

  /*
   * Permet de savoir
   * quelle page est actuellement ouverte.
   */
  const location =
    useLocation();


  return (

    <header className="navbar-container">

      {/* Logo */}
      <div className="navbar-brand">

        <h2>
          RegiMenu
        </h2>

      </div>


      {/* Navigation */}
      <nav className="nav-links">

        {/* Accueil */}
        <Link
          to="/"
          className={`
            nav-item
            ${
              location.pathname === '/'
                ? 'active'
                : ''
            }
          `}
        >
          Accueil
        </Link>


        {/* Favoris */}
        <Link
          to="/favoris"
          className={`
            nav-item
            ${
              location.pathname === '/favoris'
                ? 'active'
                : ''
            }
          `}
        >
          Favoris
        </Link>


        {/* Historique */}
        <Link
          to="/historique"
          className={`
            nav-item
            ${
              location.pathname === '/historique'
                ? 'active'
                : ''
            }
          `}
        >
          Historique
        </Link>


        {/* US4.3 : Profil */}
        <Link
          to="/profil"
          className={`
            nav-item
            ${
              location.pathname === '/profil'
                ? 'active'
                : ''
            }
          `}
        >
          Profil
        </Link>

        <Link
          to="/admin"
          className={`nav-item ${location.pathname === '/admin' ? 'active' : ''}`}
        >
          Administration
        </Link>

      </nav>


      {/* Login */}
      <div className="auth-action">

        <button
          className="login-btn"
          onClick={() => {
            if (getToken()) {
              clearToken();
              window.location.reload();
            } else {
              window.location.href = '/login';
            }
          }}
        >
          {getToken() ? 'Déconnexion' : 'Connexion'}
        </button>

      </div>

    </header>
  );
}