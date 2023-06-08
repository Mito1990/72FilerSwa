export const getCurrentUrl = () => {
    const path2 = window.location.pathname;
    const folderId = path2.substring(path2.lastIndexOf('/') + 1);
    // Check if the string contains only numbers
    if (/^[0-9]+$/.test(folderId)) {
        return parseInt(folderId);
    }
    // Check if the string contains only letters (case-insensitive)
    if (/^[a-zA-Z]+$/.test(folderId)) {
        return(folderId);
    }
    // Check if the string contains both letters and numbers
    if (/^[a-zA-Z0-9]+$/.test(folderId)) {
        return(folderId);
    }
    // Check if the string is a number
    if (!isNaN(folderId)) {
        return parseInt(folderId);
    }
};
export const checkPathForHomeOrShare =() =>{
    const path = window.location.pathname;
    const pathParts = path.split("/");
    const Value = pathParts[1];
    return Value
};