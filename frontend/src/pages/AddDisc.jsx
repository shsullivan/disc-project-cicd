import React, { useEffect, useState } from 'react';
import axios from 'axios';
import PageWrapper from "../components/PageWrapper.jsx";
import { Link } from "react-router-dom";

function AddDisc() {
    const [formData, setFormData] = useState({
        manufacturer: null,
        mold: null,
        plastic: '',
        color: '',
        condition: '',
        description: '',
        contact: {
            firstName: '',
            lastName: '',
            phone: ''
        },
        foundAt: '',
        msrp: ''
    });

    const [manufacturers, setManufacturers] = useState([]);
    const [molds, setMolds] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/manufacturers')
            .then(response => setManufacturers(response.data))
            .catch(err => console.error("Failed to fetch manufacturers", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name.startsWith('contact.')) {
            const field = name.split('.')[1];
            setFormData(prev => ({
                ...prev,
                contact: {
                    ...prev.contact,
                    [field]: value
                }
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };

    const handleManufacturerSelect = (e) => {
        const manufacturerId = parseInt(e.target.value);
        const selected = manufacturers.find(m => m.manufacturerId === manufacturerId);
        setFormData(prev => ({
            ...prev,
            manufacturer: selected,
            mold: null // reset mold selection when manufacturer changes
        }));

        axios.get(`http://localhost:8080/api/molds/by-manufacturer/${manufacturerId}`)
            .then(response => setMolds(response.data))
            .catch(err => {
                setMolds([]);
                console.error("Failed to fetch molds", err);
            });
    };

    const handleMoldSelect = (e) => {
        const moldId = parseInt(e.target.value);
        const selected = molds.find(m => m.moldId === moldId);
        setFormData(prev => ({
            ...prev,
            mold: selected
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                manufacturer: formData.manufacturer,
                mold: formData.mold,
                plastic: formData.plastic,
                color: formData.color,
                condition: parseInt(formData.condition),
                description: formData.description,
                contact: {
                    firstName: formData.contact.firstName,
                    lastName: formData.contact.lastName,
                    phone: formData.contact.phone
                },
                foundAt: formData.foundAt,
                msrp: parseFloat(formData.msrp)
            };

            await axios.post('http://localhost:8080/api/discs', payload);
            alert("Disc added successfully!");
        } catch (error) {
            console.error(error);
            alert("Error adding disc: " + (error.response?.data?.message || error.message));
        }
    };

    return (
        <PageWrapper>
            <div className="form-container">
                <h2>Add a New Disc</h2>
                <form onSubmit={handleSubmit}>
                    {/* Manufacturer dropdown */}
                    <div>
                        <label>Manufacturer</label>
                        <select onChange={handleManufacturerSelect} required>
                            <option value="">Select a manufacturer</option>
                            {manufacturers.map(m => (
                                <option key={m.manufacturerId} value={m.manufacturerId}>
                                    {m.manufacturer}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Mold dropdown */}
                    <div>
                        <label>Mold</label>
                        <select onChange={handleMoldSelect} value={formData.mold?.moldId || ''} required>
                            <option value="">Select a mold</option>
                            {molds.map(m => (
                                <option key={m.moldId} value={m.moldId}>
                                    {m.mold}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Remaining fields */}
                    {[
                        { name: 'plastic' },
                        { name: 'color' },
                        { name: 'condition', type: 'number' },
                        { name: 'description' },
                        { name: 'contact.firstName', label: 'First Name' },
                        { name: 'contact.lastName', label: 'Last Name' },
                        { name: 'contact.phone', label: 'Phone' },
                        { name: 'foundAt', label: 'Found At' },
                        { name: 'msrp', label: 'MSRP', type: 'number' }
                    ].map(({ name, type = 'text', label }) => (
                        <div key={name}>
                            <label>{label || name.charAt(0).toUpperCase() + name.slice(1)}</label>
                            <input
                                type={type}
                                name={name}
                                value={
                                    name.startsWith('contact.')
                                        ? formData.contact[name.split('.')[1]]
                                        : formData[name] || ''
                                }
                                onChange={handleChange}
                                required
                            />
                        </div>
                    ))}

                    <button type="submit">Add Disc</button>
                </form>
                <div className={"back-link"}><Link to={"/main"}>Main Menu</Link></div>
            </div>
        </PageWrapper>
    );
}

export default AddDisc;
