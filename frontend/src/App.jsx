import {
  BrowserRouter as Router,
  Routes,
  Route
} from 'react-router-dom';

import Navbar from './components/Navbar';

import Home from './pages/Home';

import RecipeDetail from './pages/RecipeDetail';

import Favoris from './pages/Favoris';

import Historique from './pages/Historique';

import Profile from './pages/Profile';

import './App.css';


function App() {

  return (

    <Router>

      {/* Navigation principale */}
      <Navbar />


      {/* Contenu des pages */}
      <main className="main-content">

        <Routes>

          {/* Accueil */}
          <Route
            path="/"
            element={<Home />}
          />


          {/* Détail d'une recette */}
          <Route
            path="/recipe/:id"
            element={<RecipeDetail />}
          />


          {/* Favoris */}
          <Route
            path="/favoris"
            element={<Favoris />}
          />


          {/* Historique */}
          <Route
            path="/historique"
            element={<Historique />}
          />


          {/* US4.3 */}
          <Route
            path="/profil"
            element={<Profile />}
          />

        </Routes>

      </main>

    </Router>
  );
}

export default App;