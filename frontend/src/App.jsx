import './App.css'
import MainApp from './pages/MainApp.jsx'
import LandingPage from './pages/DatabaseLogin.jsx'
import AddDisc from './pages/AddDisc.jsx'
import ImportDiscs from './pages/ImportDiscs.jsx'
import DeleteDisc from './pages/DeleteDisc.jsx'
import SearchByLastName from './pages/SearchByLastName.jsx'
import SearchByPhone from './pages/SearchByPhone.jsx'
import ViewAllDiscs from './pages/ViewAllDiscs.jsx'
import ViewReturnedDiscs from './pages/ViewReturnedDiscs.jsx'
import ViewSoldDiscs from './pages/ViewSoldDiscs.jsx'
import UpdateInformation from './pages/UpdateInfo.jsx'
import {Route, Routes} from "react-router-dom";
import DatabaseLogin from "./pages/DatabaseLogin.jsx";

function App() {

    return (
        <Routes>
            <Route path={"/"} element={<DatabaseLogin />} />
            <Route path={"/main"} element={<MainApp />} />
            <Route path={"/add"} element={<AddDisc />} />
            <Route path={"/import"} element={<ImportDiscs />} />
            <Route path={"/delete"} element={<DeleteDisc />} />
            <Route path={"/searchByLastName"} element={<SearchByLastName />} />
            <Route path={"/searchByPhone"} element={<SearchByPhone />} />
            <Route path={"/viewAllDiscs"} element={<ViewAllDiscs />} />
            <Route path={"/viewReturnedDiscs"} element={<ViewReturnedDiscs />} />
            <Route path={"/viewSoldDiscs"} element={<ViewSoldDiscs />} />
            <Route path={"/updateInformation"} element={<UpdateInformation />} />
        </Routes>
    )
}

export default App
