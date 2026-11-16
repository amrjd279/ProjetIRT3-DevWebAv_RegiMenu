import { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from 'react-router-dom';

import Navbar from './components/Navbar';

import Home from './pages/Home';
import RecipeDetail from './pages/RecipeDetail';
import Favoris from './pages/Favoris';
import Historique from './pages/Historique';
import Profile from './pages/Profile';
import Admin from './pages/Admin';
import Login from './pages/Login';
import Register from './pages/Register';

import './App.css';

/**
 * Notification globale affichée après une déconnexion,
 * une erreur ou une expiration de session.
 */
function ToastHost() {
  const [toast, setToast] = useState(null);

  useEffect(() => {
    const handleToast = (event) => {
      setToast(event.detail);

      window.setTimeout(() => {
        setToast(null);
      }, 4500);
    };

    window.addEventListener('regimenu:toast', handleToast);

    return () => {
      window.removeEventListener('regimenu:toast', handleToast);
    };
  }, []);

  if (!toast) {
    return null;
  }

  return (
    <div
      className={`toast toast-${toast.type || 'info'}`}
      role="status"
    >
      <span className="toast-icon">
        {toast.type === 'success' ? '✓' : '⚠'}
      </span>

      <span>{toast.message}</span>

      <button
        className="toast-close"
        onClick={() => setToast(null)}
        aria-label="Fermer la notification"
      >
        ×
      </button>
    </div>
  );
}

function App() {
  return (
    <Router>
      {/* Barre de navigation principale */}
      <Navbar />

      {/* Notifications globales */}
      <ToastHost />

      {/* Contenu principal de l'application */}
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/recipe/:id" element={<RecipeDetail />} />
          <Route path="/favoris" element={<Favoris />} />
          <Route path="/historique" element={<Historique />} />
          <Route path="/profil" element={<Profile />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;