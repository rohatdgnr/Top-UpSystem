'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Layout from '@/components/Layout';
import { documentApi, companyApi } from '@/lib/api';
import Link from 'next/link';

export default function CreateDocumentPage() {
  const router = useRouter();
  const [documentType, setDocumentType] = useState<'EFATURA' | 'EARSIV'>('EFATURA');
  const [jsonPayload, setJsonPayload] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [hasCompany, setHasCompany] = useState<boolean | null>(null);
  const [checkingCompany, setCheckingCompany] = useState(true);

  useEffect(() => {
    checkCompany();
  }, []);

  const checkCompany = async () => {
    try {
      await companyApi.getMyCompany();
      setHasCompany(true);
    } catch (err: any) {
      if (err.message?.includes('Company not found')) {
        setHasCompany(false);
      } else {
        setHasCompany(false);
      }
    } finally {
      setCheckingCompany(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const response = await documentApi.create({
        documentType,
        jsonPayload,
      });

      if (response.success) {
        router.push('/documents');
      } else {
        setError(response.message || 'Belge oluşturulamadı');
      }
    } catch (err: any) {
      if (err.message?.includes('Company not found')) {
        setError('Şirket bilgileriniz bulunamadı. Lütfen önce şirket bilgilerinizi girin.');
      } else {
        setError(err.message || 'Belge oluşturulamadı');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Layout>
      <div className="px-4 py-6 sm:px-0">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Yeni Belge Oluştur</h1>
          <p className="mt-2 text-sm text-gray-600">Yeni bir e-Fatura veya e-Arşiv belgesi oluşturun</p>
        </div>

        <div className="bg-white shadow rounded-lg p-6">
          {checkingCompany ? (
            <div className="flex items-center justify-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
            </div>
          ) : !hasCompany ? (
            <div className="space-y-4">
              <div className="bg-yellow-50 border border-yellow-400 text-yellow-700 px-4 py-3 rounded">
                <p className="font-medium">Şirket bilgileriniz bulunamadı!</p>
                <p className="text-sm mt-1">Belge oluşturabilmek için önce şirket bilgilerinizi girmeniz gerekiyor.</p>
              </div>
              <div className="flex justify-center">
                <Link
                  href="/dashboard"
                  className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  Kontrol Paneline Dön
                </Link>
              </div>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="space-y-6">
              {error && (
                <div className="bg-red-50 border border-red-400 text-red-700 px-4 py-3 rounded">
                  {error}
                </div>
              )}

            <div>
              <label htmlFor="documentType" className="block text-sm font-medium text-gray-700">
                Belge Tipi
              </label>
              <select
                id="documentType"
                value={documentType}
                onChange={(e) => setDocumentType(e.target.value as 'EFATURA' | 'EARSIV')}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
              >
                <option value="EFATURA">e-Fatura</option>
                <option value="EARSIV">e-Arşiv</option>
              </select>
            </div>

            <div>
              <label htmlFor="jsonPayload" className="block text-sm font-medium text-gray-700">
                JSON İçeriği
              </label>
              <textarea
                id="jsonPayload"
                rows={15}
                value={jsonPayload}
                onChange={(e) => setJsonPayload(e.target.value)}
                required
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm font-mono"
                placeholder='{"invoiceNumber": "INV-001", "amount": 1000, ...}'
              />
            </div>

            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => router.back()}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                İptal
              </button>
              <button
                type="submit"
                disabled={isLoading}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50"
              >
                {isLoading ? 'Oluşturuluyor...' : 'Belge Oluştur'}
              </button>
            </div>
          </form>
          )}
        </div>
      </div>
    </Layout>
  );
}

