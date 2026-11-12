import { useState } from 'react';
import {
  deleteAdminUser,
  getAdminUsers,
  suspendAdminUser
} from '../services/api';
import './Admin.css';

export default function Admin() {
  const [adminId, setAdminId] = useState(
    () => localStorage.getItem('regimenu.adminId') || ''
  );
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const loadUsers = async () => {
    if (!adminId) {
      setUsers([]);
      return;
    }
    setLoading(true);
    setError('');
    try {
      setUsers(await getAdminUsers(adminId));
      localStorage.setItem('regimenu.adminId', adminId);
    } catch (requestError) {
      setError(
        requestError.response?.data?.detail ||
        requestError.response?.data?.message ||
        'Accès refusé ou serveur indisponible.'
      );
    } finally {
      setLoading(false);
    }
  };

  const suspend = async (userId) => {
    try {
      await suspendAdminUser(adminId, userId);
      await loadUsers();
    } catch (requestError) {
      setError(
        requestError.response?.data?.detail ||
        requestError.response?.data?.message ||
        'La suspension a échoué.'
      );
    }
  };

  const remove = async (userId) => {
    if (!window.confirm('Supprimer définitivement cet utilisateur ?')) {
      return;
    }
    try {
      await deleteAdminUser(adminId, userId);
      await loadUsers();
    } catch (requestError) {
      setError(
        requestError.response?.data?.detail ||
        requestError.response?.data?.message ||
        'La suppression a échoué.'
      );
    }
  };

  return (
    <section className="admin-page">
      <div className="admin-heading">
        <div>
          <p className="admin-eyebrow">Administration</p>
          <h1>Supervision des comptes</h1>
          <p>Suivez la consommation Spoonacular et intervenez sur les comptes abusifs.</p>
        </div>
        <div className="admin-total">
          <strong>{users.reduce((total, user) => total + user.requestCount, 0)}</strong>
          <span>requêtes API</span>
        </div>
      </div>

      <form className="admin-access" onSubmit={(event) => {
        event.preventDefault();
        loadUsers();
      }}>
        <label htmlFor="admin-id">Identifiant de votre compte administrateur</label>
        <div className="admin-access-row">
          <input
            id="admin-id"
            value={adminId}
            onChange={(event) => setAdminId(event.target.value)}
            placeholder="Ex. 1"
            inputMode="numeric"
            required
          />
          <button type="submit">Charger le tableau</button>
        </div>
      </form>

      {error && <p className="admin-error" role="alert">{error}</p>}
      {loading ? (
        <p className="admin-empty">Chargement des utilisateurs...</p>
      ) : users.length === 0 ? (
        <p className="admin-empty">Aucun utilisateur à afficher.</p>
      ) : (
        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Utilisateur</th>
                <th>Rôle</th>
                <th>État</th>
                <th>Requêtes API</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>
                    <strong>{user.username}</strong>
                    <span>{user.email}</span>
                  </td>
                  <td>{user.role.replace('ROLE_', '')}</td>
                  <td><span className={`status status-${user.status.toLowerCase()}`}>{user.status}</span></td>
                  <td className="request-count">{user.requestCount}</td>
                  <td className="admin-actions">
                    <button
                      type="button"
                      className="suspend-button"
                      disabled={user.status === 'SUSPENDED' || String(user.id) === String(adminId)}
                      onClick={() => suspend(user.id)}
                    >
                      Suspendre
                    </button>
                    <button
                      type="button"
                      className="delete-button"
                      disabled={user.role === 'ROLE_ADMIN' || String(user.id) === String(adminId)}
                      onClick={() => remove(user.id)}
                    >
                      Supprimer
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
