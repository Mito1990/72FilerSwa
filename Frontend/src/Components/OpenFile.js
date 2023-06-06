import { useState } from "react";
import Cookies from 'js-cookie';

export const OpenFile = ({ currentGroup, parentFolderItem }) => {
    console.error("hello from OpenFIle")
    console.log(parentFolderItem)
    console.log(currentGroup)
    const [fileContent, setFileContent] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [rename, setRename] = useState('');
    const [isClicked, setIsClicked] = useState(false);
    const serverToken = Cookies.get('Token');

    console.log("OpenFile")
    const handleDownloadFile =(item) => {
        const folderRequest ={
            token:serverToken,
            fileID:item.id
        }
        fetch('http://localhost:8080/api/folder/file/read', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(folderRequest)
        }).then((response) => response.text()).then((data) => {
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
            folderID:parentFolderItem.folder_id,
            content:fileContent,
            token:serverToken,
            }
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
    };
    const handleClose = () => {
        setIsOpen(false);
    };
    const handleDeleteFile = async () =>{
        setIsOpen(false);
        const folderRequest = {
            folderID:parentFolderItem.folder_id,
            token:serverToken,
            }
            await fetch('http://localhost:8080/api/folder/file/delete', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
                },
                body: JSON.stringify(folderRequest)
            }).then((response) => response.json()).then((data) => {
                // getUpdate.getUpdate(data);
            }).catch((error) => {
                console.error('Error retrieving data:', error);
            });
            console.log("-----------------------------------------------------")
    }
    const handleRenameFile =(e)=>{
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
                folderID:parentFolderItem.folder_id,
                token:serverToken,
            }
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
                // getUpdate.getUpdate(data);
            }catch(error){
                console.error('Error retrieving data:', error);
            };
        }
    };
    return (
        <div className="">
        {!isOpen ? (
        <button className="flex flex-col justify-center item-center m-6"onClick={() => handleDownloadFile(parentFolderItem)}>
            <svg className="h-9 w-9 "xmlns="http://www.w3.org/2000/svg"height="1em"viewBox="0 0 384 512"><path d="M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128z" /></svg>
            <p className="pl-3 h-6 w-6 flex justify-center items-center text-center">{parentFolderItem.name}</p>
        </button>
        ) : (
        <div className="flex flex-col h-full">
            <div className="flex items-center justify-between px-4 py-2 bg-gray-800 text-white">
            {!isClicked?<button className="px-3 py-1 text-xl font-medium hover:bg-blue-600 text-white rounded"onClick={setClicked}>{parentFolderItem.name}</button>:<input className=" text-slate-950" onKeyDown={handleKeyDown} type="text" value={rename} onChange={handleRenameFile}></input>}
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