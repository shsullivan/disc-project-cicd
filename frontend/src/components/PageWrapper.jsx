

import React from 'react';
import './PageWrapper.css'; // CSS for consistent styling

function PageWrapper({ children }) {
    return (
        <div className="page-container">
            <div className="overlay-box">
                {children}
            </div>
        </div>
    );
}

export default PageWrapper;