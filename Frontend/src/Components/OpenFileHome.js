import { useState } from "react";
import Cookies from 'js-cookie';

export const OpenFileHome = ({ update,currentFolder}) => {
    console.error("OpenFileHome => item")
    console.error(currentFolder)
    const [fileContent, setFileContent] = useState('');
    const [rename, setRename] = useState('');
    const [isClicked, setIsClicked] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const serverToken = Cookies.get('Token');

    const handleDownloadFile =(item) => {
        const folderRequest ={
        folderID:item.folder_id,
        token:serverToken,
        shared:false,
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
        setIsClicked(false);
        const folderRequest ={
            folderID:currentFolder.folder_id,
            content:fileContent,
            token:serverToken,
            shared:false,
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
            parent:currentFolder.parent,
            path:currentFolder.path,
            folderID:currentFolder.folder_id,
            token:serverToken,
            shared:false,
            file:true
            }
            console.log("---Delete---")
            console.log(folderRequest);
                try{
                const response = await fetch('http://localhost:8080/api/folder/file/delete', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+serverToken
                    },
                    body: JSON.stringify(folderRequest)
                })
                const data = await response.json();
                update.update(data);
            }catch(error){
                console.error('Error retrieving data:', error);
            };
            console.log("-----------------------------------------------------")
    }
    const handleRenameFile = async(e)=>{
        setRename(e.target.value);
        console.log(rename);
    }
    const setClicked = ()=>{
        setIsClicked(true);
    }
    const handleKeyDown = async (e) => {
        console.error("hello from handleKeyDown")
        if (e.keyCode === 13) {
            setIsClicked(false);
            const folderRequest ={
                name:rename,
                parent:currentFolder.parent,
                path:currentFolder.path,
                folderID:currentFolder.folder_id,
                token:serverToken,
                shared:false,
                file:true
            }
            console.error("folderRequest")
            try{
                const response = await fetch('http://localhost:8080/api/folder/file/rename', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+serverToken
                    },
                    body: JSON.stringify(folderRequest)
                })
                const data = await response.json();
                update.update(data);
            }catch(error){
                console.error('Error retrieving data:', error);
            };
            console.log("-----------------------------------------------------")
        }
    };
    return (
        <div className="">
        {!isOpen ? (
        <button className="flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full"onClick={() => handleDownloadFile(currentFolder)}>
            <svg className="h-9 w-9 bg-slate-500"xmlns="http://www.w3.org/2000/svg"height="1em"viewBox="0 0 384 512"><path d="M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128z" /></svg>
            <div className="h-5 w-9">{currentFolder.name}</div>
        </button>
        ) : (
        <div className="flex flex-col h-96 w-1/2">
            <div className="flex items-center justify-between px-4 py-2 bg-gray-800 text-white">
                {!isClicked?<button className="px-3 py-1 text-xl font-medium hover:bg-blue-600 text-white rounded"onClick={setClicked}>{currentFolder.name}</button>:<input className=" text-slate-950" onKeyDown={handleKeyDown} type="text" value={rename} onChange={handleRenameFile}></input>}
                <div className="flex flex-row">
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onClick={handleSaveFile}>Save</button>
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onChange={handleDeleteFile}>Delete</button>
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"onClick={handleClose}>Close</button>
                </div>
            </div>
            <div className="flex-1 p-4 bg-white h-full">
                <textarea className="w-full h-full bg-gray-100 p-2 text-sm leading-snug resize-none focus:outline-none focus:bg-white focus:shadow-outline"value={fileContent}onChange={handleFileChange}></textarea>
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