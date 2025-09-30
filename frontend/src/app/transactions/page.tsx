'use client';

import { useState, useEffect } from 'react';
import { transactionService, customerService, Transaction, Customer } from '@/lib/api';
import { toast } from 'react-hot-toast';
import { ArrowRightLeft, Search, History } from 'lucide-react';

// Helper function para formatear fechas correctamente
const formatDateForDisplay = (dateString: string) => {
  // Crear fecha directamente desde el string sin conversión de zona horaria
  const [year, month, day] = dateString.split('-');
  return `${day}/${month}/${year}`;
};

// Helper function para obtener fecha actual en formato YYYY-MM-DD
const getCurrentDateString = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

export default function TransactionsPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showTransferForm, setShowTransferForm] = useState(false);
  const [searchAccount, setSearchAccount] = useState('');
  const [transferData, setTransferData] = useState({
    senderAccountNumber: '',
    receiverAccountNumber: '',
    amount: 0,
    transactionDate: getCurrentDateString()
  });

  // Cargar clientes
  const fetchCustomers = async () => {
    try {
      const response = await customerService.getAll();
      setCustomers(response.data);
    } catch (error) {
      toast.error('Error al cargar clientes');
      console.error(error);
    }
  };

  // Buscar transacciones por cuenta
  const searchTransactions = async () => {
    if (!searchAccount.trim()) {
      toast.error('Ingresa un número de cuenta');
      return;
    }

    try {
      setIsLoading(true);
      const response = await transactionService.getByAccountNumber(searchAccount);
      setTransactions(response.data);
      
      if (response.data.length === 0) {
        toast('No se encontraron transacciones para esta cuenta', {
          icon: 'ℹ️',
        });
      }
    } catch (error) {
      toast.error('Error al buscar transacciones');
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  // Realizar transferencia
  const handleTransfer = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (transferData.senderAccountNumber === transferData.receiverAccountNumber) {
      toast.error('La cuenta origen y destino no pueden ser iguales');
      return;
    }

    if (transferData.amount <= 0) {
      toast.error('El monto debe ser mayor a 0');
      return;
    }

    try {
      await transactionService.transfer(transferData);
      toast.success('Transferencia realizada exitosamente');
      setShowTransferForm(false);
      setTransferData({
        senderAccountNumber: '',
        receiverAccountNumber: '',
        amount: 0,
        transactionDate: getCurrentDateString()
      });
      fetchCustomers(); // Actualizar saldos
    } catch (error) {
      toast.error('Error al realizar transferencia');
      console.error(error);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-4 sm:py-8">
        {/* Header responsive */}
        <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center mb-6 sm:mb-8 gap-4">
          <div className="flex items-center gap-2 sm:gap-3">
            <ArrowRightLeft className="h-6 w-6 sm:h-8 sm:w-8 text-green-600" />
            <h1 className="text-2xl sm:text-3xl font-bold text-gray-900">Transacciones</h1>
          </div>
          <button
            onClick={() => setShowTransferForm(true)}
            className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg flex items-center justify-center gap-2 transition-colors w-full sm:w-auto"
          >
            <ArrowRightLeft className="h-4 w-4" />
            <span className="text-sm sm:text-base">Nueva Transferencia</span>
          </button>
        </div>

      {/* Formulario de transferencia */}
      {showTransferForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white p-4 sm:p-6 rounded-lg max-w-md w-full shadow-2xl max-h-[90vh] overflow-y-auto">
            <h2 className="text-lg sm:text-xl font-bold mb-4 text-gray-800">Nueva Transferencia</h2>
            <form onSubmit={handleTransfer} className="space-y-4">
              <div>
                <label className="block text-sm font-semibold mb-1 text-gray-700">Cuenta Origen</label>
                <select
                  value={transferData.senderAccountNumber}
                  onChange={(e) => setTransferData({...transferData, senderAccountNumber: e.target.value})}
                  className="w-full p-3 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-800 bg-white"
                  required
                >
                  <option value="" className="text-gray-600">Seleccionar cuenta origen</option>
                  {customers.map((customer) => (
                    <option key={customer.id} value={customer.accountNumber} className="text-gray-800">
                      {customer.accountNumber} - {customer.firstName} {customer.lastName} (${customer.balance.toFixed(2)})
                    </option>
                  ))}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-semibold mb-1 text-gray-700">Cuenta Destino</label>
                <select
                  value={transferData.receiverAccountNumber}
                  onChange={(e) => setTransferData({...transferData, receiverAccountNumber: e.target.value})}
                  className="w-full p-3 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-800 bg-white"
                  required
                >
                  <option value="" className="text-gray-600">Seleccionar cuenta destino</option>
                  {customers
                    .filter(customer => customer.accountNumber !== transferData.senderAccountNumber)
                    .map((customer) => (
                    <option key={customer.id} value={customer.accountNumber} className="text-gray-800">
                      {customer.accountNumber} - {customer.firstName} {customer.lastName}
                    </option>
                  ))}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-semibold mb-1 text-gray-700">Monto</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={transferData.amount}
                  onChange={(e) => setTransferData({...transferData, amount: parseFloat(e.target.value)})}
                  className="w-full p-3 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-800 bg-white"
                  required
                />
              </div>
              
              <div>
                <label className="block text-sm font-semibold mb-1 text-gray-700">Fecha</label>
                <input
                  type="date"
                  value={transferData.transactionDate}
                  onChange={(e) => setTransferData({...transferData, transactionDate: e.target.value})}
                  className="w-full p-3 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-800 bg-white"
                  required
                />
              </div>
              
              <div className="flex flex-col sm:flex-row gap-3">
                <button
                  type="submit"
                  className="flex-1 bg-green-600 hover:bg-green-700 text-white py-3 px-4 rounded-lg transition-colors text-sm sm:text-base font-medium"
                >
                  Transferir
                </button>
                <button
                  type="button"
                  onClick={() => setShowTransferForm(false)}
                  className="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-700 py-3 px-4 rounded-lg transition-colors text-sm sm:text-base font-medium"
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Búsqueda de transacciones */}
      <div className="bg-white rounded-lg shadow p-6 mb-8 border border-gray-200">
        <div className="flex items-center gap-3 mb-4">
          <Search className="h-5 w-5 text-green-600" />
          <h2 className="text-lg font-semibold text-gray-800">Buscar Historial de Transacciones</h2>
        </div>
        
        {/* Search responsive */}
        <div className="flex flex-col sm:flex-row gap-3">
          <select
            value={searchAccount}
            onChange={(e) => setSearchAccount(e.target.value)}
            className="flex-1 p-3 border-2 border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-800 bg-white text-sm sm:text-base"
          >
            <option value="" className="text-gray-600">Seleccionar cuenta para ver historial</option>
            {customers.map((customer) => (
              <option key={customer.id} value={customer.accountNumber} className="text-gray-800">
                {customer.accountNumber} - {customer.firstName} {customer.lastName}
              </option>
            ))}
          </select>
          <button
            onClick={searchTransactions}
            disabled={isLoading}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white px-4 sm:px-6 py-3 rounded-lg flex items-center justify-center gap-2 transition-colors w-full sm:w-auto"
          >
            {isLoading ? (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            ) : (
              <History className="h-4 w-4" />
            )}
            <span className="text-sm sm:text-base">Buscar</span>
          </button>
        </div>
      </div>

      {/* Lista de transacciones */}
      {transactions.length > 0 && (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="px-4 sm:px-6 py-4 border-b border-gray-200 bg-green-50">
            <h3 className="text-lg font-semibold text-gray-900">Historial de Transacciones</h3>
          </div>
          
          {/* Vista móvil - Cards */}
          <div className="block sm:hidden">
            {transactions.map((transaction) => (
              <div key={transaction.id} className="border-b border-gray-200 p-4 hover:bg-green-50">
                <div className="flex justify-between items-start mb-2">
                  <div className="flex-1">
                    <div className="text-sm font-semibold text-gray-800 mb-1">
                      {formatDateForDisplay(transaction.transactionDate)}
                    </div>
                    <div className="text-xs text-gray-600">
                      De: <span className="font-medium">{transaction.senderAccountNumber}</span>
                    </div>
                    <div className="text-xs text-gray-600">
                      Para: <span className="font-medium">{transaction.receiverAccountNumber}</span>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-lg font-bold text-green-700 mb-1">
                      ${transaction.amount.toFixed(2)}
                    </div>
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                      transaction.senderAccountNumber === searchAccount 
                        ? 'bg-red-100 text-red-800' 
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {transaction.senderAccountNumber === searchAccount ? 'Enviado' : 'Recibido'}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Vista escritorio - Tabla */}
          <div className="hidden sm:block overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-green-600">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Fecha
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Origen
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Destino
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Monto
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider">
                    Tipo
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {transactions.map((transaction) => (
                  <tr key={transaction.id} className="hover:bg-green-50 border-b border-gray-100">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-800">
                      {formatDateForDisplay(transaction.transactionDate)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-800">
                      {transaction.senderAccountNumber}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-800">
                      {transaction.receiverAccountNumber}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-bold text-green-700">
                      ${transaction.amount.toFixed(2)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                        transaction.senderAccountNumber === searchAccount 
                          ? 'bg-red-100 text-red-800' 
                          : 'bg-green-100 text-green-800'
                      }`}>
                        {transaction.senderAccountNumber === searchAccount ? 'Enviado' : 'Recibido'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
      </div>
    </div>
  );
}
