import Link from 'next/link';
import { Users, ArrowRightLeft, TrendingUp, DollarSign } from 'lucide-react';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-16">
        {/* Header */}
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            Sistema Bancario
          </h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            Gestiona clientes y transacciones de manera sencilla y segura. 
            Plataforma completa para operaciones bancarias.
          </p>
        </div>

        {/* Statistics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-16">
          <div className="bg-white rounded-lg shadow-lg p-6 text-center">
            <div className="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Users className="h-8 w-8 text-blue-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900">Clientes</h3>
            <p className="text-gray-600 mt-2">Gestión completa de clientes</p>
          </div>
          
          <div className="bg-white rounded-lg shadow-lg p-6 text-center">
            <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <ArrowRightLeft className="h-8 w-8 text-green-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900">Transferencias</h3>
            <p className="text-gray-600 mt-2">Movimientos de dinero seguros</p>
          </div>
          
          <div className="bg-white rounded-lg shadow-lg p-6 text-center">
            <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <TrendingUp className="h-8 w-8 text-purple-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900">Reportes</h3>
            <p className="text-gray-600 mt-2">Historial y análisis</p>
          </div>
        </div>

        {/* Action Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          <Link href="/customers" className="group">
            <div className="bg-white rounded-lg shadow-lg p-8 hover:shadow-xl transition-all duration-300 transform group-hover:-translate-y-1">
              <div className="flex items-center justify-between mb-6">
                <div className="bg-blue-600 w-12 h-12 rounded-lg flex items-center justify-center">
                  <Users className="h-6 w-6 text-white" />
                </div>
                <div className="text-blue-600 group-hover:translate-x-1 transition-transform">
                  →
                </div>
              </div>
              <h2 className="text-2xl font-bold text-gray-900 mb-3">
                Gestión de Clientes
              </h2>
              <p className="text-gray-600 mb-4">
                Crea, actualiza y administra la información de tus clientes. 
                Consulta saldos y datos personales.
              </p>
              <ul className="text-sm text-gray-500 space-y-1">
                <li>• Crear nuevos clientes</li>
                <li>• Editar información personal</li>
                <li>• Consultar saldos de cuenta</li>
                <li>• Eliminar registros</li>
              </ul>
            </div>
          </Link>

          <Link href="/transactions" className="group">
            <div className="bg-white rounded-lg shadow-lg p-8 hover:shadow-xl transition-all duration-300 transform group-hover:-translate-y-1">
              <div className="flex items-center justify-between mb-6">
                <div className="bg-green-600 w-12 h-12 rounded-lg flex items-center justify-center">
                  <ArrowRightLeft className="h-6 w-6 text-white" />
                </div>
                <div className="text-green-600 group-hover:translate-x-1 transition-transform">
                  →
                </div>
              </div>
              <h2 className="text-2xl font-bold text-gray-900 mb-3">
                Transacciones
              </h2>
              <p className="text-gray-600 mb-4">
                Realiza transferencias entre cuentas y consulta el historial 
                completo de movimientos.
              </p>
              <ul className="text-sm text-gray-500 space-y-1">
                <li>• Transferir dinero entre cuentas</li>
                <li>• Consultar historial de transacciones</li>
                <li>• Filtrar por número de cuenta</li>
                <li>• Ver movimientos enviados y recibidos</li>
              </ul>
            </div>
          </Link>
        </div>

        {/* Features */}
        <div className="mt-20 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-8">
            Características Principales
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="bg-blue-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <DollarSign className="h-6 w-6 text-blue-600" />
              </div>
              <h3 className="font-semibold text-gray-900">Seguro</h3>
              <p className="text-sm text-gray-600 mt-1">Transacciones protegidas</p>
            </div>
            
            <div className="text-center">
              <div className="bg-green-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <Users className="h-6 w-6 text-green-600" />
              </div>
              <h3 className="font-semibold text-gray-900">Fácil de usar</h3>
              <p className="text-sm text-gray-600 mt-1">Interfaz intuitiva</p>
            </div>
            
            <div className="text-center">
              <div className="bg-purple-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <TrendingUp className="h-6 w-6 text-purple-600" />
              </div>
              <h3 className="font-semibold text-gray-900">Tiempo real</h3>
              <p className="text-sm text-gray-600 mt-1">Actualizaciones instantáneas</p>
            </div>
            
            <div className="text-center">
              <div className="bg-orange-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <ArrowRightLeft className="h-6 w-6 text-orange-600" />
              </div>
              <h3 className="font-semibold text-gray-900">Completo</h3>
              <p className="text-sm text-gray-600 mt-1">Todas las funciones necesarias</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
