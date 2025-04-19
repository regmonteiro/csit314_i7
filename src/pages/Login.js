import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/Login.css';

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  const togglePassword = () => setShowPassword(prev => !prev);

  return (
    <div className="login-container">
      <h2 className="login-title">Login</h2>
      <form className="login-form">
        <div className="login-form-group">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            placeholder="Enter your email"
            required
            autoComplete="email"
          />
        </div>

        <div className="login-form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type={showPassword ? 'text' : 'password'}
            placeholder="Enter your password"
            required
            autoComplete="current-password"
          />
          <button
            type="button"
            className="toggle-password"
            onClick={togglePassword}
            aria-label="Toggle password visibility"
          >
            {showPassword ? 'Hide' : 'Show'}
          </button>
        </div>

        <button type="submit" className="login-button">Login</button>

        <div className="login-footer">
          <p>
            Don’t have an account? <Link to="/signup">Sign up</Link>
          </p>
          <p>
            Forgot your password? <Link to="/forgot-password">Reset it</Link>
          </p>
        </div>
      </form>
    </div>
  );
}

export default Login;
