import React, { useState } from 'react';
import PageWrapper from '../components/PageWrapper.jsx';
import { Link } from 'react-router-dom';

function UpdateDisc() {
    const [discID, setDiscID] = useState('');
    const [formData, setFormData] = useState(null);
    const [message, setMessage] = useState('');

    const fetchDisc = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/discs/${discID}`);
            if (response.ok) {
                const data = await response.json();
                setFormData(data);
                setMessage('');
            } else {
                setMessage('Disc not found.');
                setFormData(null);
            }
        } catch (error) {
            setMessage('Error fetching disc.');
            setFormData(null);
        }
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleNestedChange = (section, field, value) => {
        setFormData(prev => ({
            ...prev,
            [section]: {
                ...prev[section],
                [field]: value
            }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/discs/${discID}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                setMessage('Disc updated successfully!');
            } else {
                setMessage('Failed to update disc.');
            }
        } catch (error) {
            setMessage('Error: ' + error.message);
        }
    };

    return (
        <PageWrapper>
            <div className="form-container">
                <h2>Update Disc</h2>
                {!formData && (
                    <div>
                        <label>Disc ID: </label>
                        <input
                            type="text"
                            value={discID}
                            onChange={(e) => setDiscID(e.target.value)}
                            required
                        />
                        <button onClick={fetchDisc}>Load Disc</button>
                    </div>
                )}

                {formData && (
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Manufacturer</label>
                            <input
                                type="text"
                                value={formData.manufacturer?.manufacturer || ''}
                                onChange={(e) =>
                                    handleNestedChange('manufacturer', 'manufacturer', e.target.value)
                                }
                            />
                        </div>

                        <div>
                            <label>Mold</label>
                            <input
                                type="text"
                                value={formData.mold?.mold || ''}
                                onChange={(e) =>
                                    handleNestedChange('mold', 'mold', e.target.value)
                                }
                            />
                        </div>

                        <div>
                            <label>Plastic</label>
                            <input
                                type="text"
                                name="plastic"
                                value={formData.plastic || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Color</label>
                            <input
                                type="text"
                                name="color"
                                value={formData.color || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Condition</label>
                            <input
                                type="number"
                                name="condition"
                                value={formData.condition || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Description</label>
                            <input
                                type="text"
                                name="description"
                                value={formData.description || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Contact First Name</label>
                            <input
                                type="text"
                                value={formData.contact?.firstName || ''}
                                onChange={(e) =>
                                    handleNestedChange('contact', 'firstName', e.target.value)
                                }
                            />
                        </div>

                        <div>
                            <label>Contact Last Name</label>
                            <input
                                type="text"
                                value={formData.contact?.lastName || ''}
                                onChange={(e) =>
                                    handleNestedChange('contact', 'lastName', e.target.value)
                                }
                            />
                        </div>

                        <div>
                            <label>Contact Phone</label>
                            <input
                                type="text"
                                value={formData.contact?.phone || ''}
                                onChange={(e) =>
                                    handleNestedChange('contact', 'phone', e.target.value)
                                }
                            />
                        </div>

                        <div>
                            <label>Found At</label>
                            <input
                                type="text"
                                name="foundAt"
                                value={formData.foundAt || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>MSRP</label>
                            <input
                                type="number"
                                name="msrp"
                                step="0.01"
                                value={formData.msrp || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Resale Value</label>
                            <input
                                type="number"
                                name="resaleValue"
                                step="0.01"
                                value={formData.resaleValue || ''}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Returned</label>
                            <input
                                type="checkbox"
                                name="returned"
                                checked={formData.returned || false}
                                onChange={handleChange}
                            />
                        </div>

                        <div>
                            <label>Sold</label>
                            <input
                                type="checkbox"
                                name="sold"
                                checked={formData.sold || false}
                                onChange={handleChange}
                            />
                        </div>

                        <button type="submit">Update Disc</button>
                    </form>
                )}

                {message && <p>{message}</p>}

                <div className="back-link">
                    <Link to="/main">← Back to Main Menu</Link>
                </div>
            </div>
        </PageWrapper>
    );
}

export default UpdateDisc;
