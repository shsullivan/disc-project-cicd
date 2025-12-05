import React, { useEffect, useState } from 'react';
import PageWrapper from '../components/PageWrapper.jsx';
import {Link} from "react-router-dom";

function ViewReturnedDiscs() {
    const [discs, setDiscs] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/discs/returned')
            .then(response => response.json())
            .then(data => setDiscs(data))
            .catch(error => console.error('Error fetching returned discs:', error));
    }, []);
    const totalMSRP = discs.reduce((sum, disc) => sum + disc.msrp, 0).toFixed(2);

    return (
        <div className="table-container">
            <h2>Returned Discs</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Manufacturer</th>
                    <th>Mold</th>
                    <th>Plastic</th>
                    <th>Color</th>
                    <th>Condition</th>
                    <th>Description</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Phone</th>
                    <th>Found At</th>
                    <th>Returned</th>
                    <th>Sold</th>
                    <th>MSRP</th>
                    <th>Resale</th>
                </tr>
                </thead>
                <tbody>
                {discs.map(disc => (
                    <tr key={disc.discID}>
                        <td>{disc.discID}</td>
                        <td>{disc.manufacturer?.manufacturer}</td>
                        <td>{disc.mold?.mold}</td>
                        <td>{disc.plastic}</td>
                        <td>{disc.color}</td>
                        <td>{disc.condition}</td>
                        <td>{disc.description}</td>
                        <td>{disc.contact?.firstName}</td>
                        <td>{disc.contact?.lastName}</td>
                        <td>{disc.contact?.phone}</td>
                        <td>{disc.foundAt}</td>
                        <td>{disc.returned ? 'Yes' : 'No'}</td>
                        <td>{disc.sold ? 'Yes' : 'No'}</td>
                        <td>${disc.msrp.toFixed(2)}</td>
                        <td>${disc.resaleValue.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <p><strong>Total MSRP of Returned Discs:</strong> ${totalMSRP}</p>
            <div className={"back-link"}><Link to={"/main"}>Main Menu</Link></div>
        </div>
    );
}

export default ViewReturnedDiscs;
