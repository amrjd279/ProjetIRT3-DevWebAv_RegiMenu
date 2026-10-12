import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';

export default function Navbar() {
  const location = useLocation();

  return (
    <header className="navbar-container">
      <div className="navbar-brand">
        <h2>RegiMenu</h2>
      </div>
      <nav className="nav-links">
        <Link to="/" className={`nav-item ${location.pathname === '/' ? 'active' : ''}`}>Accueil</Link>
        <Link to="/favoris" className={`nav-item ${location.pathname === '/favoris' ? 'active' : ''}`}>Favoris</Link>
        <Link to="/historique" className={`nav-item ${location.pathname === '/historique' ? 'active' : ''}`}>Historique</Link>
      </nav>
      <div className="auth-action">
        <button className="login-btn" onClick={() => alert("Redirection vers Zitadel...")}>
          Login
        </button>
      </div>
    </header>
  );
}