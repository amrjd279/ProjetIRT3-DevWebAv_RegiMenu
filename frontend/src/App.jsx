import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import RecipeDetail from './pages/RecipeDetail';
import Favoris from './pages/Favoris';
import Historique from './pages/Historique';
import './App.css';

function App() {
  return (
    <Router>
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/recipe/:id" element={<RecipeDetail />} />
          <Route path="/favoris" element={<Favoris />} />
          <Route path="/historique" element={<Historique />} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;