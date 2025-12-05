import { useState } from 'react';
import { useNavigate } from "react-router-dom";

function DatabaseLogin() {
    const [host, setHost] = useState("");
    const [port, setPort] = useState("3306");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch("http://localhost:8080/api/db/connect", {
            method: 'POST',
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({host, port, username, password}),
        });

        if (response.ok) {
            navigate("/main");
        }
        else {
            alert("Database connection failed. Please check your credentials and try again.");
        }
    };

    return (
            <div className={"overlay"}>
                <form onSubmit={handleSubmit}>
                    <h2>Connect to Database</h2>
                    <input placeholder={"Host"} value={host} onChange={(e) => setHost(e.target.value)} required />
                    <input placeholder={"Port"} value={port} onChange={(e) => setPort(e.target.value)} required />
                    <input placeholder={"Username"} value={username} onChange={(e) => setUsername(e.target.value)} required />
                    <input placeholder={"Password"} type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    <button type="submit">Connect</button>
                </form>
            </div>
    );
}
export default DatabaseLogin;