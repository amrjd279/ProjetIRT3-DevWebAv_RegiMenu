const FAVORITES_KEY = 'regimenu:favorites';
const HISTORY_KEY = 'regimenu:history';

const readList = (key) => {
  try {
    const value = window.localStorage.getItem(key);
    const parsed = value ? JSON.parse(value) : [];
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
};

const writeList = (key, value) => {
  window.localStorage.setItem(key, JSON.stringify(value));
  window.dispatchEvent(new StorageEvent('storage', { key }));
};

export const getFavorites = () => readList(FAVORITES_KEY);

export const isFavorite = (id) => getFavorites().some((meal) => String(meal.id) === String(id));

export const toggleFavorite = (meal) => {
  const favorites = getFavorites();
  const alreadyFavorite = favorites.some((item) => String(item.id) === String(meal.id));
  const nextFavorites = alreadyFavorite
    ? favorites.filter((item) => String(item.id) !== String(meal.id))
    : [meal, ...favorites];

  writeList(FAVORITES_KEY, nextFavorites);
  return !alreadyFavorite;
};

export const removeFavorite = (id) => {
  writeList(
    FAVORITES_KEY,
    getFavorites().filter((meal) => String(meal.id) !== String(id))
  );
};

export const getHistory = () => readList(HISTORY_KEY);

export const addToHistory = (meal) => {
  const history = getHistory().filter((item) => String(item.id) !== String(meal.id));
  writeList(HISTORY_KEY, [{ ...meal, viewedAt: new Date().toISOString() }, ...history].slice(0, 30));
};

export const clearHistory = () => writeList(HISTORY_KEY, []);
