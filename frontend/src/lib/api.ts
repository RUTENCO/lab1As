import axios from 'axios';

// Configuración base de axios
const baseURL = process.env.NEXT_PUBLIC_API_URL 
  ? `${process.env.NEXT_PUBLIC_API_URL}/api`
  : 'http://localhost:8080/api'; // En desarrollo apunta al Spring Boot local

// Debug: mostrar la URL que se está usando
console.log('🚀 API Base URL:', baseURL);
console.log('🌍 Environment:', process.env.NODE_ENV);
console.log('🔗 NEXT_PUBLIC_API_URL:', process.env.NEXT_PUBLIC_API_URL);

const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Debug: interceptor para mostrar todas las peticiones
api.interceptors.request.use(
  (config) => {
    console.log('📡 Making request to:', config.url);
    console.log('🔧 Full URL:', `${config.baseURL}${config.url}`);
    return config;
  },
  (error) => {
    console.error('❌ Request error:', error);
    return Promise.reject(error);
  }
);

// Debug: interceptor para mostrar respuestas y errores
api.interceptors.response.use(
  (response) => {
    console.log('✅ Response received from:', response.config.url);
    return response;
  },
  (error) => {
    console.error('❌ Response error:', error.message);
    console.error('🔍 Error details:', error.response?.data || error);
    return Promise.reject(error);
  }
);

// Tipos TypeScript
export interface Customer {
  id?: number;
  firstName: string;
  lastName: string;
  accountNumber: string;
  balance: number;
}

export interface Transaction {
  id?: number;
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
  transactionDate: string;
}

// Servicios de Customer
export const customerService = {
  // Obtener todos los clientes
  getAll: () => api.get<Customer[]>('/customers'),
  
  // Obtener cliente por ID
  getById: (id: number) => api.get<Customer>(`/customers/${id}`),
  
  // Obtener cliente por número de cuenta
  getByAccountNumber: (accountNumber: string) => 
    api.get<Customer>(`/customers/account/${accountNumber}`),
  
  // Crear cliente
  create: (customer: Omit<Customer, 'id'>) => 
    api.post<Customer>('/customers', customer),
  
  // Actualizar cliente
  update: (id: number, customer: Partial<Customer>) => 
    api.put<Customer>(`/customers/${id}`, customer),
  
  // Eliminar cliente
  delete: (id: number) => api.delete(`/customers/${id}`),
};

// Servicios de Transaction
export const transactionService = {
  // Transferir dinero
  transfer: (transaction: Omit<Transaction, 'id'>) => 
    api.post<Transaction>('/transactions/transfer', transaction),
  
  // Obtener transacciones por número de cuenta
  getByAccountNumber: (accountNumber: string) => 
    api.get<Transaction[]>(`/transactions/${accountNumber}`),
};
