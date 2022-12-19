import React from "react";

const ClientOnly = ({ children, ...delegated }) => {
    const [hasMounted, setHasMounted] = React.useState(false);
    React.useEffect(() => {
        if (typeof window !== "undefined") {
            setHasMounted(true);
        }
    }, []);

    if (!hasMounted) 
        return null;
    return (
        <React.Fragment {...delegated}>
            <link href="https://fonts.googleapis.com/css2?family=Mulish&display=swap" rel="stylesheet"/>
            {children}
        </React.Fragment>
    );
};
export default ClientOnly;
