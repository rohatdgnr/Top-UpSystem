'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import Layout from '@/components/Layout';
import { adminApi, packageApi } from '@/lib/api';

export default function AdminPage() {
  const { user, isLoading: authLoading } = useAuth();
  const router = useRouter();
  const [activeTab, setActiveTab] = useState<'users' | 'companies' | 'transactions' | 'documents' | 'packages' | 'loadCredit'>('users');
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showPackageModal, setShowPackageModal] = useState(false);
  const [showCreditModal, setShowCreditModal] = useState(false);
  const [packageForm, setPackageForm] = useState({ name: '', creditAmount: '', price: '', description: '' });
  const [creditForm, setCreditForm] = useState({ companyId: '', amount: '', description: '' });

  useEffect(() => {
    if (!authLoading) {
      if (!user || user.role !== 'ADMIN') {
        router.push('/dashboard');
        return;
      }
      if (activeTab === 'packages') {
        loadPackages();
      } else if (activeTab !== 'loadCredit') {
        loadData();
      } else {
        setData([]); // Clear data when switching to loadCredit tab
      }
    }
  }, [user, authLoading, activeTab]);

  const loadData = async () => {
    setLoading(true);
    setData([]); // Clear previous data
    try {
      let response;
      switch (activeTab) {
        case 'users':
          response = await adminApi.getUsers();
          break;
        case 'companies':
          response = await adminApi.getCompanies();
          break;
        case 'transactions':
          response = await adminApi.getTransactions();
          break;
        case 'documents':
          response = await adminApi.getDocuments();
          break;
      }
      if (response?.success && response.data) {
        setData(response.data as any[]);
      } else {
        setData([]);
      }
    } catch (error) {
      console.error('Veri yükleme hatası:', error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  const loadPackages = async () => {
    setLoading(true);
    setData([]); // Clear previous data
    try {
      const response = await packageApi.getAllForAdmin();
      console.log('Paket response:', response); // Debug log
      if (response?.success && response.data) {
        console.log('Paketler:', response.data); // Debug log
        setData(response.data as any[]);
      } else {
        console.warn('Paket response başarısız veya data yok:', response);
        setData([]);
      }
    } catch (error) {
      console.error('Paket yükleme hatası:', error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePackage = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await packageApi.create({
        name: packageForm.name,
        creditAmount: parseFloat(packageForm.creditAmount),
        price: parseFloat(packageForm.price),
        description: packageForm.description,
        isActive: true,
      });
      setShowPackageModal(false);
      setPackageForm({ name: '', creditAmount: '', price: '', description: '' });
      alert('Kredi paketi başarıyla oluşturuldu!');
      await loadPackages(); // Paketleri yeniden yükle
    } catch (error: any) {
      alert(error.message || 'Kredi paketi oluşturulamadı');
    }
  };

  const handleLoadCredit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await adminApi.loadCredit({
        companyId: parseInt(creditForm.companyId),
        amount: parseFloat(creditForm.amount),
        description: creditForm.description || 'Admin tarafından yüklendi',
      });
      setShowCreditModal(false);
      setCreditForm({ companyId: '', amount: '', description: '' });
      alert('Kredi başarıyla yüklendi!');
    } catch (error: any) {
      alert(error.message || 'Kredi yüklenemedi');
    }
  };

  const getTableHeaders = () => {
    if (data.length === 0) return [];
    
    const firstItem = data[0];
    return Object.keys(firstItem).map(key => {
      const translations: { [key: string]: string } = {
        id: 'ID',
        name: 'Ad',
        email: 'E-posta',
        role: 'Rol',
        status: 'Durum',
        createdAt: 'Oluşturulma',
        taxNo: 'Vergi No',
        title: 'Ünvan',
        creditBalance: 'Kredi Bakiyesi',
        amount: 'Tutar',
        type: 'Tip',
        description: 'Açıklama',
        documentType: 'Belge Tipi',
        companyId: 'Şirket ID',
        entegratorTrackingId: 'Takip No',
        updatedAt: 'Güncellenme',
      };
      return translations[key] || key;
    });
  };

  const formatValue = (key: string, value: any): string => {
    if (value === null || value === undefined) return '-';
    if (typeof value === 'boolean') return value ? 'Evet' : 'Hayır';
    if (key === 'status') {
      const statusMap: { [key: string]: string } = {
        ACTIVE: 'Aktif',
        INACTIVE: 'Pasif',
        SENT: 'Gönderildi',
        PENDING: 'Beklemede',
        FAILED: 'Başarısız',
      };
      return statusMap[value] || value;
    }
    if (key === 'role') {
      const roleMap: { [key: string]: string } = {
        ADMIN: 'Yönetici',
        USER: 'Kullanıcı',
      };
      return roleMap[value] || value;
    }
    if (key === 'documentType') {
      return value === 'EFATURA' ? 'e-Fatura' : 'e-Arşiv';
    }
    if (key.includes('At') && typeof value === 'string') {
      return new Date(value).toLocaleString('tr-TR');
    }
    if (typeof value === 'object') return JSON.stringify(value);
    return String(value);
  };

  if (authLoading || (loading && activeTab !== 'packages' && activeTab !== 'loadCredit')) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="px-4 py-6 sm:px-0">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Yönetim Paneli</h1>
          <p className="mt-2 text-sm text-gray-600">Kullanıcıları, şirketleri ve sistem verilerini yönetin</p>
        </div>

        <div className="bg-white shadow rounded-lg">
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex">
              {([
                { key: 'users', label: 'Kullanıcılar' },
                { key: 'companies', label: 'Şirketler' },
                { key: 'transactions', label: 'İşlemler' },
                { key: 'documents', label: 'Belgeler' },
                { key: 'packages', label: 'Kredi Paketleri' },
                { key: 'loadCredit', label: 'Kredi Yükle' },
              ] as const).map((tab) => (
                <button
                  key={tab.key}
                  onClick={() => setActiveTab(tab.key as any)}
                  className={`${
                    activeTab === tab.key
                      ? 'border-indigo-500 text-indigo-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  } whitespace-nowrap py-4 px-6 border-b-2 font-medium text-sm`}
                >
                  {tab.label}
                </button>
              ))}
            </nav>
          </div>

          <div className="p-6">
            {activeTab === 'packages' && (
              <div>
                <div className="mb-4 flex justify-between items-center">
                  <h2 className="text-lg font-medium text-gray-900">Kredi Paketleri</h2>
                  <button
                    onClick={() => setShowPackageModal(true)}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-indigo-700"
                  >
                    Yeni Paket Oluştur
                  </button>
                </div>
                {loading ? (
                  <div className="flex justify-center py-8">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
                  </div>
                ) : data.length === 0 ? (
                  <p className="text-sm text-gray-600 mb-4">
                    Henüz kredi paketi oluşturulmamış. Yeni paket oluşturmak için yukarıdaki butona tıklayın.
                  </p>
                ) : (
                  <div className="overflow-x-auto mt-4">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            ID
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Paket Adı
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Kredi Miktarı
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Fiyat (TL)
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Durum
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Açıklama
                          </th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {data.map((pkg: any) => (
                          <tr key={pkg.id}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{pkg.id}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{pkg.name || '-'}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{pkg.creditAmount || '-'}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {pkg.price ? parseFloat(pkg.price).toFixed(2) + ' ₺' : '-'}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span
                                className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                  pkg.isActive
                                    ? 'bg-green-100 text-green-800'
                                    : 'bg-red-100 text-red-800'
                                }`}
                              >
                                {pkg.isActive ? 'Aktif' : 'Pasif'}
                              </span>
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-500">{pkg.description || '-'}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {activeTab === 'loadCredit' && (
              <div>
                <div className="mb-4 flex justify-between items-center">
                  <h2 className="text-lg font-medium text-gray-900">Kredi Yükleme</h2>
                  <button
                    onClick={() => setShowCreditModal(true)}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-indigo-700"
                  >
                    Kredi Yükle
                  </button>
                </div>
                <p className="text-sm text-gray-600 mb-4">
                  Bir şirkete kredi yüklemek için yukarıdaki butona tıklayın.
                </p>
                <div className="mt-4">
                  <h3 className="text-md font-medium text-gray-900 mb-2">Şirket Listesi:</h3>
                  {activeTab === 'loadCredit' && (
                    <div className="mt-2">
                      <button
                        onClick={() => {
                          setActiveTab('companies');
                          setTimeout(() => setActiveTab('loadCredit'), 100);
                        }}
                        className="text-indigo-600 hover:text-indigo-900 text-sm"
                      >
                        Şirket listesini görmek için tıklayın
                      </button>
                    </div>
                  )}
                </div>
              </div>
            )}

            {(activeTab !== 'packages' && activeTab !== 'loadCredit') && (
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      {getTableHeaders().map((header, idx) => (
                        <th
                          key={idx}
                          className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                        >
                          {header}
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {data.map((item, index) => (
                      <tr key={index}>
                        {Object.entries(item).map(([key, value], idx) => (
                          <td key={idx} className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {formatValue(key, value)}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>

        {/* Package Modal */}
        {showPackageModal && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Yeni Kredi Paketi Oluştur</h3>
                <form onSubmit={handleCreatePackage} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Paket Adı</label>
                    <input
                      type="text"
                      required
                      value={packageForm.name}
                      onChange={(e) => setPackageForm({ ...packageForm, name: e.target.value })}
                      placeholder="Örn: Standart Paket"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Kredi Miktarı</label>
                    <input
                      type="number"
                      required
                      min="1"
                      value={packageForm.creditAmount}
                      onChange={(e) => setPackageForm({ ...packageForm, creditAmount: e.target.value })}
                      placeholder="Örn: 100"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Fiyat (TL)</label>
                    <input
                      type="number"
                      required
                      min="0"
                      step="0.01"
                      value={packageForm.price}
                      onChange={(e) => setPackageForm({ ...packageForm, price: e.target.value })}
                      placeholder="Örn: 99.99"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Açıklama</label>
                    <textarea
                      value={packageForm.description}
                      onChange={(e) => setPackageForm({ ...packageForm, description: e.target.value })}
                      placeholder="Paket açıklaması (opsiyonel)"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      rows={3}
                    />
                  </div>
                  <div className="flex justify-end space-x-3">
                    <button
                      type="button"
                      onClick={() => setShowPackageModal(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                    >
                      İptal
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                    >
                      Oluştur
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}

        {/* Credit Load Modal */}
        {showCreditModal && (
          <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
              <div className="mt-3">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Kredi Yükle</h3>
                <form onSubmit={handleLoadCredit} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Şirket ID</label>
                    <input
                      type="number"
                      required
                      min="1"
                      value={creditForm.companyId}
                      onChange={(e) => setCreditForm({ ...creditForm, companyId: e.target.value })}
                      placeholder="Örn: 1"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Kredi Miktarı</label>
                    <input
                      type="number"
                      required
                      min="1"
                      step="0.01"
                      value={creditForm.amount}
                      onChange={(e) => setCreditForm({ ...creditForm, amount: e.target.value })}
                      placeholder="Örn: 50.00"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Açıklama (Opsiyonel)</label>
                    <textarea
                      value={creditForm.description}
                      onChange={(e) => setCreditForm({ ...creditForm, description: e.target.value })}
                      placeholder="Kredi yükleme açıklaması"
                      className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      rows={3}
                    />
                  </div>
                  <div className="flex justify-end space-x-3">
                    <button
                      type="button"
                      onClick={() => setShowCreditModal(false)}
                      className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                    >
                      İptal
                    </button>
                    <button
                      type="submit"
                      className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                    >
                      Yükle
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
}
