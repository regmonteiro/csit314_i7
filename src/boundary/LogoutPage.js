import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LogoutController } from '../controllers/LogoutController';
import '../styles/general.css';

function LogoutPage() {
  const navigate = useNavigate();

  useEffect(() => {
    LogoutController.processLogout();
    const timer = setTimeout(() => navigate('/login'), 2000);
    return () => clearTimeout(timer);
  }, [navigate]);

  return (
    <div className="logout-container">
      <h2>You have been logged out</h2>
      <p className="logout-message">Thank you for using our service. Redirecting to login...</p>
    </div>
  );
}

export default LogoutPage;