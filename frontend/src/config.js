let token = localStorage.getItem('token') || null;

export const setToken = (newToken) => {
  token = newToken;
  localStorage.setItem('token', newToken);
};

export const getToken = () => {
  return token;
};

export const getBearerToken = () => {
  return 'Bearer ' + token;
};
console.log(token)
