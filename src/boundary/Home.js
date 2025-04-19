import React from 'react';
import '../styles/Home.css';
import { useNavigate } from 'react-router-dom';

function Home() {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <header className="hero">
        <h1>Welcome to CSIT314</h1>
        <p>Discover your dream home today.</p>
        <button className="cta-button" onClick={() => navigate('/login')}>
          Login Here
        </button>
      </header>

      <section className="features">
        <div className="feature-card">
          <h3>Luxury Homes</h3>
          <p>Handpicked premium properties just for you.</p>
        </div>
        <div className="feature-card">
          <h3>Smart Search</h3>
          <p>Find exactly what you're looking for with our intelligent filters.</p>
        </div>
        <div className="feature-card">
          <h3>Expert Agents</h3>
          <p>Our experienced agents are ready to help.</p>
        </div>
      </section>
    </div>
  );
}

export default Home;
