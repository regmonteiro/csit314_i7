export class UserAccount {
  static login(email, password) {
    // dummy data, replace with backend calls
    if (email === 'admin@example.com' && password === 'password123') {
      localStorage.setItem('token', 'mock-token-abc123');
      localStorage.setItem('userId', 'admin001');
      return true;
    }
    return false;
  }

  static logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
  }

  static isLoggedIn() {
    return !!localStorage.getItem('token');
  }
}