import {Link} from "react-router-dom";

function MainApp() {
    return (
        <div className="landing-container">
            <div className={"overlay"}>
                <h1>D.I.S.C. Main Menu</h1>
                <ul style={{ listStyle: 'none', padding: '0' }}>
                    <li><Link to={"/add"}>Add a new disc</Link></li>
                    <li><Link to={"/import"}>Import discs from a file</Link></li>
                    <li><Link to={"/delete"}>Delete a disc from your inventory</Link></li>
                    <li><Link to={"/viewAllDiscs"}>View all discs</Link></li>
                    <li><Link to={"/viewReturnedDiscs"}>View discs you've returned</Link></li>
                    <li><Link to={"/viewSoldDiscs"}>View discs you've sold</Link></li>
                    <li><Link to={"/searchByLastName"}>Search discs by last name</Link></li>
                    <li><Link to={"/searchByPhone"}>Search discs by phone number</Link></li>
                    <li><Link to={"/updateInformation"}>Update Disc Information</Link></li>
                </ul>
            </div>
        </div>
    )
}

export default MainApp