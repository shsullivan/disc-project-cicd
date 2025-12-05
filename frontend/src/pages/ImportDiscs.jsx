import React, { useState } from 'react';
import PageWrapper from '../components/PageWrapper.jsx';
import { Link } from 'react-router-dom';

function ImportDiscs() {
    const [file, setFile] = useState(null);
    const [result, setResult] = useState(null);
    const [error, setError] = useState('');

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setResult(null);
        setError('');
    };

    const handleImport = async (e) => {
        e.preventDefault();

        if (!file) {
            setError('Please select a file first.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch('http://localhost:8080/api/discs/import', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                const data = await response.json();
                setResult(data);
                setError('');
            } else {
                setError('Failed to import discs.');
            }
        } catch (err) {
            setError('An error occurred: ' + err.message);
        }
    };

    return (
        <PageWrapper>
            <div className="form-container">
                <h2>Import Discs from File</h2>
                <form onSubmit={handleImport}>
                    <input type="file" accept=".txt" onChange={handleFileChange} />
                    <button type="submit">Import</button>
                </form>

                {error && <p style={{ color: 'red' }}>{error}</p>}

                {result && (
                    <div className="import-results">
                        <h3>Import Summary</h3>
                        <p><strong>Successes:</strong> {result.successes.length}</p>
                        <ul>
                            {result.successes.map((line, index) => (
                                <li key={index}>{line}</li>
                            ))}
                        </ul>

                        <p><strong>Failures:</strong> {result.failures.length}</p>
                        <ul style={{ color: 'red' }}>
                            {result.failures.map((line, index) => (
                                <li key={index}>{line}</li>
                            ))}
                        </ul>
                    </div>
                )}

                <div className="back-link">
                    <Link to="/main">← Back to Main Menu</Link>
                </div>
            </div>
        </PageWrapper>
    );
}

export default ImportDiscs;
