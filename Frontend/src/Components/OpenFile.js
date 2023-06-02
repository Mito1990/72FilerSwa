import { useState } from "react";
import Cookies from 'js-cookie';

export const OpenFile = ({ up, onDataToParent }) => {
    console.log(onDataToParent)
    const [fileContent, setFileContent] = useState('');
    const [itemFromParent] = useState(onDataToParent[0]);
    const [currentGroupId] = useState(onDataToParent[1]);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');
    console.log("OpenFile")
    console.log("------------------------")
    console.log("filename")
    console.log(itemFromParent.folder_id);
    console.warn(onDataToParent[0].parent)
    const handleDownloadFile =(item) => {
        const folderRequest ={
        folderID:item.folder_id,
        groupID:currentGroupId,
        token:serverToken,
        shared:true,
        file:true
        }
        console.log(folderRequest);
        fetch('http://localhost:8080/api/folder/file/read', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(folderRequest)
        }).then((response) => response.text()).then((data) => {
            console.log(data);
            setFileContent(data);
            setIsOpen(true);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
        console.log("-----------------------------------------------------")
    };

    const handleFileChange = (event) => {
        setFileContent(event.target.value);
    };

    const handleSaveFile = () => {
        const folderRequest ={
            folderID:itemFromParent.folder_id,
            groupID:currentGroupId,
            content:fileContent,
            token:serverToken,
            shared:true,
            file:true
            }
            console.log("---save---")
            console.log(folderRequest);
            fetch('http://localhost:8080/api/folder/file/write', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
                },
                body: JSON.stringify(folderRequest)
            }).then((response) => response.text()).then((data) => {
                console.log("sajdkhaskjdhaskjdhakj")
                console.log(data);
                setFileContent(data);
                setIsOpen(false);
            }).catch((error) => {
                console.error('Error retrieving data:', error);
            });
            console.log("-----------------------------------------------------")
      // Logic to save the file content
      // You can make a separate API request to send the updated file content back to the server
      // Or implement the necessary logic to save the file on the server directly
    };
    const handleClose = () => {
        setIsOpen(false);
    };
    const handleDeleteFile = async () =>{
        setIsOpen(false);
        const folderRequest = {
            groupID:currentGroupId,
            parent:itemFromParent.parent,
            path:itemFromParent.path,
            folderID:itemFromParent.folder_id,
            token:serverToken,
            shared:true,
            file:true
            }
            console.log("---Delete---")
            console.log(folderRequest);
            await fetch('http://localhost:8080/api/folder/file/delete', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
                },
                body: JSON.stringify(folderRequest)
            }).then((response) => response.json()).then((data) => {
                // setFileContent(data);
                up.getUpdate(data);
            }).catch((error) => {
                console.error('Error retrieving data:', error);
            });
            console.log("-----------------------------------------------------")
    }
    return (
        <div className="">
        {!isOpen ? (
        <button className="flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full"onClick={() => handleDownloadFile(itemFromParent)}>
            <svg className="h-9 w-9 bg-slate-500"xmlns="http://www.w3.org/2000/svg"height="1em"viewBox="0 0 384 512"><path d="M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128z" /></svg>
            <div className="h-5 w-9">{itemFromParent.name}</div>
        </button>
        ) : (
        <div className="flex flex-col h-full">
            <div className="flex items-center justify-between px-4 py-2 bg-gray-800 text-white">
                <h1 className="text-xl font-bold">Text Editor</h1>
                <div className="flex flex-row">
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onClick={handleSaveFile}>Save</button>
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onClick={handleDeleteFile}>Delete</button>
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onClick={handleClose}>Close</button>
                </div>
            </div>
            <div className="flex-1 p-4 bg-white">
                <textarea className="w-full min-h-full bg-gray-100 p-2 text-sm leading-snug resize-none focus:outline-none focus:bg-white focus:shadow-outline"value={fileContent}onChange={handleFileChange}style={{ height: `${fileContent.split('\n').length * 1.2}em` }}></textarea>
            </div>
        </div>
        )}
    </div>
    );
};

       // <div className="flex flex-col items-center justify-center">
                //     <button onClick={handleClose} className="text-white hover:text-gray-700  m-2">Close</button>
                //     <textarea className="mt-4 p-2 w-full h-64 border rounded" value={fileContent}onChange={handleFileChange}/>
                //     <button className="mt-4 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"  onClick={handleSaveFile}>Save File</button>
                // </div>