import React, { useState } from 'react';
import PageWrapper from '../components/PageWrapper.jsx';
import { Link } from 'react-router-dom';

function DeleteDisc() {
    const [discID, setDiscID] = useState('');
    const [message, setMessage] = useState('');

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/discs/${discID}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                setMessage(`Disc ${discID} was successfully deleted.`);
            } else if (response.status === 404) {
                setMessage(`Disc with ID ${discID} not found.`);
            } else {
                setMessage(`Error deleting disc ${discID}.`);
            }
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <PageWrapper>
            <div className="form-container">
                <h2>Delete a Disc</h2>
                <form onSubmit={handleDelete}>
                    <label htmlFor="discID">Disc ID:</label>
                    <input
                        type="number"
                        id="discID"
                        value={discID}
                        onChange={(e) => setDiscID(e.target.value)}
                        required
                    />
                    <button type="submit">Delete Disc</button>
                </form>
                {message && <p>{message}</p>}
                <div className="back-link">
                    <Link to="/main">← Back to Main Menu</Link>
                </div>
            </div>
        </PageWrapper>
    );
}

export default DeleteDisc;
