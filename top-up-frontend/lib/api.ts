const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
}

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
    
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'An error occurred' }));
      throw new Error(error.message || 'Request failed');
    }

    return response.json();
  }

  async get<T>(endpoint: string): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'GET' });
  }

  async post<T>(endpoint: string, data?: any): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async put<T>(endpoint: string, data?: any): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async delete<T>(endpoint: string): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, { method: 'DELETE' });
  }
}

export const apiClient = new ApiClient(API_BASE_URL);

// Auth API
export const authApi = {
  register: (data: { name: string; email: string; password: string; taxNo: string; companyTitle: string }) =>
    apiClient.post('/auth/register', data),
  
  login: (data: { email: string; password: string }) =>
    apiClient.post('/auth/login', data),
};

// Company API
export const companyApi = {
  create: (data: any) => apiClient.post('/companies', data),
  getMyCompany: () => apiClient.get('/companies/me'),
  update: (data: any) => apiClient.put('/companies/me', data),
};

// Wallet API
export const walletApi = {
  getMyWallet: () => apiClient.get('/wallet/me'),
  loadCredit: (data: any) => apiClient.post('/wallet/load', data),
};

// Package API
export const packageApi = {
  getAll: () => apiClient.get('/packages'),
  getAllForAdmin: () => apiClient.get('/packages/all'),
  create: (data: any) => apiClient.post('/packages', data),
};

// Document API
export const documentApi = {
  create: (data: any) => apiClient.post('/documents', data),
  send: (id: number) => apiClient.post(`/documents/${id}/send`),
  getAll: () => apiClient.get('/documents'),
  getById: (id: number) => apiClient.get(`/documents/${id}`),
};

// Admin API
export const adminApi = {
  getUsers: () => apiClient.get('/admin/users'),
  getCompanies: () => apiClient.get('/admin/companies'),
  getTransactions: () => apiClient.get('/admin/transactions'),
  getDocuments: () => apiClient.get('/admin/documents'),
  loadCredit: (data: { companyId: number; amount: number; description?: string }) =>
    apiClient.post('/wallet/load', data),
};

