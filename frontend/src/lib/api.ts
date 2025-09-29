import axios from 'axios';

// Configuración base de axios
const api = axios.create({
  baseURL: process.env.NODE_ENV === 'production' 
    ? '/api' // En producción (Vercel) usará el mismo dominio
    : 'http://localhost:8080/api', // En desarrollo apunta al Spring Boot local
  headers: {
    'Content-Type': 'application/json',
  },
});

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
