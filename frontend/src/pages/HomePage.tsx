import type { CSSProperties, MouseEvent } from "react";
import { Link } from "react-router-dom";

import logo from "../assets/logo.jpeg";
import heroImage from "../assets/heroImage.jpg";

import "../css/HomePage.css";
export default function HomePage() {
    function handleHeroMouseMove(event: MouseEvent<HTMLElement>) {
    const hero = event.currentTarget;
    const bounds = hero.getBoundingClientRect();

    const x = ((event.clientX - bounds.left) / bounds.width) * 100;
    const y = ((event.clientY - bounds.top) / bounds.height) * 100;

    hero.style.setProperty("--cursor-x", `${x}%`);
    hero.style.setProperty("--cursor-y", `${y}%`);
  }

  function handleHeroMouseLeave(event: MouseEvent<HTMLElement>) {
    event.currentTarget.style.setProperty("--cursor-x", "50%");
    event.currentTarget.style.setProperty("--cursor-y", "50%");
  }

  return (
    <div className="home-page">
      <header className="home-header">
        <Link
          className="home-logo-link"
          to="/"
          aria-label="Lasyankali home"
        >
          <img
            className="home-logo"
            src={logo}
            alt="Lasyankali"
          />
        </Link>

        <Link className="home-title" to="/">
          Lasyankali<span>: NrityaSutra</span>
        </Link>

        <Link className="home-login home-login-header" to="/login">
          Login
        </Link>
      </header>

      <main className="home-main">
        <section
          className="home-hero"
          aria-labelledby="hero-heading"
          onMouseMove={handleHeroMouseMove}
          onMouseLeave={handleHeroMouseLeave}
          style={
            {
              "--hero-image": `url("${heroImage}")`,
            } as CSSProperties
          }
        >
          <div className="home-hero-image" aria-hidden="true" />
          <div className="home-hero-overlay" aria-hidden="true" />

          <div className="home-hero-content">
            <p className="home-eyebrow">
              Art. Tradition. Connection.
            </p>

            <h1 id="hero-heading">
              Experience the rhythm.
              <span>Stay connected to your academy.</span>
            </h1>

            <p className="home-description">
              Lasyankali celebrates the beauty of Indian classical
              dance through learning, expression, and tradition.
            </p>

            <p className="home-description">
              NrityaSutra brings students, parents, and teachers
              together with one place for academy activities,
              events, and fee information.
            </p>

            <Link className="home-login home-login-hero" to="/login">
              Login to NrityaSutra
              <span aria-hidden="true"> →</span>
            </Link>
          </div>
        </section>
      </main>

      <footer className="home-footer">
        <p>
          © {new Date().getFullYear()} Lasyankali: NrityaSutra.
          All rights reserved.
        </p>

        <p className="home-footer-note">
          Rooted in tradition. Connected through technology.
        </p>
      </footer>
    </div>
  );
}