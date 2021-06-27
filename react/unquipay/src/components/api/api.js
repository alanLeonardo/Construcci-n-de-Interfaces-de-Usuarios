import axios from 'axios';

axios.defaults.baseURL = "http://localhost:7000";
axios.defaults.timeout = 20000; // 20s
axios.defaults.headers.post['Content-Type'] = 'application/json';

const createTransfer = ({ fromCVU, toCVU, amount }) => {
  return axios.post('/transfer', { fromCVU: fromCVU, toCVU: toCVU, amount: amount })
  .then(response => { 
    return Promise.resolve(response);
  })
  .catch(error => {
      return Promise.reject(error.response.data);
  });
}

const createUser = ({email,firstName,lastName,idCard,password}) => {
  return axios.post('/users/', {
    email: email,
    firstName: firstName,
    lastName: lastName,
    idCard: idCard,
    password: password
  })
  .then(response => {
    return Promise.resolve(response);
  })
  .catch(error => {
    return Promise.reject(error.response.data);
  });
}

const createCashIn = ({ amount, isCreditCard, cardNumber, fullName, securityCode, endDate, fromCVU}) => {
  return axios.post('/cashin', 
  {
    amount: amount,
    isCreditCard: isCreditCard,
    cardNumber: cardNumber,
    fullName: fullName,
    securityCode: securityCode,
    endDate: endDate,
    fromCVU: fromCVU
  })
  .then(response => {
    return Promise.resolve(response);
  })
  .catch(error => {
    return Promise.reject(error.response.data);
  }) 
}

const loginUser = ({email, password}) => {
  return axios.post('/login',
  {
    email: email,
    password: password
  })
  .then(response => {
    return Promise.resolve(response);
  })
  .catch(error => {
    return Promise.reject(error.response.data);
  })
}

const getTransactions = (cvu) => {
  return axios.get(`/transactions/${cvu}`)
  .then(response => {
    return Promise.resolve(response);
  })
  .catch(error => {
    return Promise.reject(error.response.data);
  })
}

const getUserByCVU = (cvu) => {
  return axios.get(`users/${cvu}`)
  .then(response => {
    return Promise.resolve(response);
  })
  .catch(error => {
    return Promise.reject(error.response.data);
  })
}

export {createUser, createCashIn, createTransfer, loginUser, getTransactions, getUserByCVU}