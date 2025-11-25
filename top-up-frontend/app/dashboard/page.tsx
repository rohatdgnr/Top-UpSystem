'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import Layout from '@/components/Layout';
import { walletApi, documentApi, companyApi } from '@/lib/api';

export default function DashboardPage() {
  const { user, isLoading: authLoading } = useAuth();
  const router = useRouter();
  const [wallet, setWallet] = useState<any>(null);
  const [documents, setDocuments] = useState<any[]>([]);
  const [company, setCompany] = useState<any>(null);
  const [showCompanyForm, setShowCompanyForm] = useState(false);
  const [companyForm, setCompanyForm] = useState({ 
    taxNo: '', 
    title: '',
    taxOffice: '',
    address: '',
    phone: '',
    email: '',
    gibSenderAccount: '',
    gibReceiverAccount: ''
  });
  const [stats, setStats] = useState({
    total: 0,
    sent: 0,
    pending: 0,
    failed: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!authLoading && !user) {
      router.push('/login');
      return;
    }

    if (user) {
      loadData();
    }
  }, [user, authLoading]);

  const loadData = async () => {
    try {
      // Load company first
      try {
        const companyRes = await companyApi.getMyCompany();
        if (companyRes.success && companyRes.data) {
          setCompany(companyRes.data);
          setShowCompanyForm(false);
        }
      } catch (err: any) {
        if (err.message?.includes('Company not found')) {
          setCompany(null);
          setShowCompanyForm(true);
        } else {
          setCompany(null);
        }
      }
      
      // Load wallet and documents
      const [walletRes, documentsRes] = await Promise.all([
        walletApi.getMyWallet().catch(() => ({ success: false })),
        documentApi.getAll().catch(() => ({ success: false, data: [] })),
      ]);

      if (walletRes.success) setWallet(walletRes.data);
      if (documentsRes.success) {
        const docs = documentsRes.data as any[];
        setDocuments(docs);
        setStats({
          total: docs.length,
          sent: docs.filter((d) => d.status === 'SENT').length,
          pending: docs.filter((d) => d.status === 'PENDING').length,
          failed: docs.filter((d) => d.status === 'FAILED').length,
        });
      }
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveCompany = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (company) {
        // Update existing company
        await companyApi.update({
          taxNo: companyForm.taxNo,
          title: companyForm.title,
          taxOffice: companyForm.taxOffice,
          address: companyForm.address,
          phone: companyForm.phone,
          email: companyForm.email,
          gibSenderAccount: companyForm.gibSenderAccount,
          gibReceiverAccount: companyForm.gibReceiverAccount,
        });
      } else {
        // Create new company
        await companyApi.create({
          taxNo: companyForm.taxNo,
          title: companyForm.title,
          taxOffice: companyForm.taxOffice,
          address: companyForm.address,
          phone: companyForm.phone,
          email: companyForm.email,
          gibSenderAccount: companyForm.gibSenderAccount,
          gibReceiverAccount: companyForm.gibReceiverAccount,
        });
      }
      setShowCompanyForm(false);
      await loadData();
    } catch (err: any) {
      alert(err.message || 'Şirket bilgileri kaydedilemedi');
    }
  };

  const handleEditCompany = () => {
    if (company) {
      setCompanyForm({
        taxNo: company.taxNo || '',
        title: company.title || '',
        taxOffice: company.taxOffice || '',
        address: company.address || '',
        phone: company.phone || '',
        email: company.email || '',
        gibSenderAccount: company.gibSenderAccount || '',
        gibReceiverAccount: company.gibReceiverAccount || '',
      });
    }
    setShowCompanyForm(true);
  };

  if (authLoading || loading) {
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
          <h1 className="text-3xl font-bold text-gray-900">Kontrol Paneli</h1>
          <p className="mt-2 text-sm text-gray-600">Hoş geldiniz, {user?.name}!</p>
        </div>

        {/* Şirket Bilgileri Bölümü */}
        <div className="bg-white shadow rounded-lg p-6 mb-8">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-900">Şirket Bilgileri</h2>
            {company && !showCompanyForm && (
              <button
                onClick={handleEditCompany}
                className="px-4 py-2 text-sm font-medium text-indigo-600 hover:text-indigo-800"
              >
                Düzenle
              </button>
            )}
          </div>

          {showCompanyForm ? (
            <form onSubmit={handleSaveCompany} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Vergi No *</label>
                  <input
                    type="text"
                    required
                    minLength={10}
                    maxLength={11}
                    value={companyForm.taxNo}
                    onChange={(e) => setCompanyForm({ ...companyForm, taxNo: e.target.value })}
                    placeholder="10 veya 11 haneli vergi no"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Şirket Ünvanı *</label>
                  <input
                    type="text"
                    required
                    value={companyForm.title}
                    onChange={(e) => setCompanyForm({ ...companyForm, title: e.target.value })}
                    placeholder="Şirket ünvanı"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Vergi Dairesi</label>
                  <input
                    type="text"
                    value={companyForm.taxOffice}
                    onChange={(e) => setCompanyForm({ ...companyForm, taxOffice: e.target.value })}
                    placeholder="Vergi dairesi"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Adres</label>
                  <input
                    type="text"
                    value={companyForm.address}
                    onChange={(e) => setCompanyForm({ ...companyForm, address: e.target.value })}
                    placeholder="Adres"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Telefon</label>
                  <input
                    type="text"
                    value={companyForm.phone}
                    onChange={(e) => setCompanyForm({ ...companyForm, phone: e.target.value })}
                    placeholder="Telefon"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">E-posta</label>
                  <input
                    type="email"
                    value={companyForm.email}
                    onChange={(e) => setCompanyForm({ ...companyForm, email: e.target.value })}
                    placeholder="E-posta"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">GIB Gönderen Hesap</label>
                  <input
                    type="text"
                    value={companyForm.gibSenderAccount}
                    onChange={(e) => setCompanyForm({ ...companyForm, gibSenderAccount: e.target.value })}
                    placeholder="GIB gönderen hesap"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">GIB Alıcı Hesap</label>
                  <input
                    type="text"
                    value={companyForm.gibReceiverAccount}
                    onChange={(e) => setCompanyForm({ ...companyForm, gibReceiverAccount: e.target.value })}
                    placeholder="GIB alıcı hesap"
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  />
                </div>
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => {
                    setShowCompanyForm(false);
                    if (company) {
                      setCompanyForm({
                        taxNo: company.taxNo || '',
                        title: company.title || '',
                        taxOffice: company.taxOffice || '',
                        address: company.address || '',
                        phone: company.phone || '',
                        email: company.email || '',
                        gibSenderAccount: company.gibSenderAccount || '',
                        gibReceiverAccount: company.gibReceiverAccount || '',
                      });
                    }
                  }}
                  className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  İptal
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  {company ? 'Güncelle' : 'Kaydet'}
                </button>
              </div>
            </form>
          ) : company ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <span className="text-sm font-medium text-gray-500">Vergi No:</span>
                <p className="text-sm text-gray-900">{company.taxNo}</p>
              </div>
              <div>
                <span className="text-sm font-medium text-gray-500">Şirket Ünvanı:</span>
                <p className="text-sm text-gray-900">{company.title}</p>
              </div>
              {company.taxOffice && (
                <div>
                  <span className="text-sm font-medium text-gray-500">Vergi Dairesi:</span>
                  <p className="text-sm text-gray-900">{company.taxOffice}</p>
                </div>
              )}
              {company.address && (
                <div>
                  <span className="text-sm font-medium text-gray-500">Adres:</span>
                  <p className="text-sm text-gray-900">{company.address}</p>
                </div>
              )}
              {company.phone && (
                <div>
                  <span className="text-sm font-medium text-gray-500">Telefon:</span>
                  <p className="text-sm text-gray-900">{company.phone}</p>
                </div>
              )}
              {company.email && (
                <div>
                  <span className="text-sm font-medium text-gray-500">E-posta:</span>
                  <p className="text-sm text-gray-900">{company.email}</p>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center py-4">
              <p className="text-sm text-gray-500 mb-4">Şirket bilgileriniz henüz kayıtlı değil.</p>
              <button
                onClick={() => setShowCompanyForm(true)}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                Şirket Bilgilerini Ekle
              </button>
            </div>
          )}
        </div>

        {/* Dashboard İçeriği */}
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8">
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <div className="text-2xl font-bold text-gray-900">
                    {wallet?.creditBalance || '0'}
                  </div>
                  <div className="text-sm text-gray-500">Kredi Bakiyesi</div>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="text-2xl font-bold text-gray-900">{stats.total}</div>
              <div className="text-sm text-gray-500">Toplam Belge</div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="text-2xl font-bold text-green-600">{stats.sent}</div>
              <div className="text-sm text-gray-500">Gönderilen Belgeler</div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="text-2xl font-bold text-red-600">{stats.failed}</div>
              <div className="text-sm text-gray-500">Başarısız Belgeler</div>
            </div>
          </div>
        </div>

        <div className="bg-white shadow rounded-lg">
          <div className="px-4 py-5 sm:p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Son Belgeler</h2>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Tip
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Durum
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Oluşturulma Tarihi
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {documents.slice(0, 5).map((doc) => (
                    <tr key={doc.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {doc.documentType === 'EFATURA' ? 'e-Fatura' : 'e-Arşiv'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            doc.status === 'SENT'
                              ? 'bg-green-100 text-green-800'
                              : doc.status === 'PENDING'
                              ? 'bg-yellow-100 text-yellow-800'
                              : 'bg-red-100 text-red-800'
                          }`}
                        >
                          {doc.status === 'SENT' ? 'GÖNDERİLDİ' : doc.status === 'PENDING' ? 'BEKLEMEDE' : 'BAŞARISIZ'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(doc.createdAt).toLocaleDateString('tr-TR')}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

