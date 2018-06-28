import axios from 'axios'

let rest = axios.create({
   baseURL: process.env.REACT_APP_MODE === 'production' ? "/api/" : "http://localhost:8080/api/"
});

export default rest;