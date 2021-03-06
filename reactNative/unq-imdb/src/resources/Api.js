import axios from 'axios';

const API = 'https://us-central1-unq-imdb.cloudfunctions.net/api/v1';
const GROUP_ID = '3';

const headers = { 'content-type': 'application/json' };

const execute = (method, url, data) => axios({ method, url: `${API}/${GROUP_ID}/${url}`, headers, data, })
  .then(response => response.data)
  .catch(response => Promise.reject(response.response.data));

export default {
  login: (user) => execute('POST', 'login', user),
  register: (user) => execute('POST', 'register', user),
  getTop: () => execute('GET', 'top'),
  search: (text) => execute('GET', `search?q=${text}`),
  getMovie: (movieID) => execute('GET', `/${movieID}`),
  comment: (movieID, comment) => execute('PUT', `${movieID}/comment`, comment),
  getTopByGenre: (genreName) => execute('GET', `genre/${genreName}`),
  getMoreMoviesByGenre: (nPage) => execute('GET', `top?page=${nPage}`)
};
