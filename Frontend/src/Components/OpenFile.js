import { useState } from "react";
import Cookies from 'js-cookie';

export const OpenFile = ({   dataFromOpenFile, currentState }) => {
    const [fileContent, setFileContent] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [rename, setRename] = useState('');
    const [isClicked, setIsClicked] = useState(false);
    const serverToken = Cookies.get('Token');
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
    };

    const handleFileChange = (event) => {
        setFileContent(event.target.value);
    };

    const handleSaveFile = () => {
        const folderRequest ={
            token:serverToken,
            fileID: currentState.id,
            content:fileContent
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
            folderID:currentState.id,
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
            dataFromOpenFile.dataFromOpenFile("hello world!")
            }).catch((error) => {
                console.error('Error retrieving data:', error);
            });
            dataFromOpenFile.dataFromOpenFile()
    }
    const handleRenameFile =(e)=>{
        setRename(e.target.value);
    }
    const setClicked = ()=>{
        setIsClicked(true);
    }
    const handleKeyDown = async (e) => {
        if (e.keyCode === 13) {
            setIsClicked(false);
            const folderRequest ={
                rename:rename,
                id:currentState.id,
                token:serverToken,
            }
            try{
                const response = await fetch('http://localhost:8080/api/folder/file/rename/fileElement', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+serverToken
                    },
                    body: JSON.stringify(folderRequest)
                })
                const data = await response.json();
                dataFromOpenFile.dataFromOpenFile()
            }catch(error){
                console.error('Error retrieving data:', error);
            };
        }
    };

    return (
        <div className="">
        {!isOpen ? (
        <button className="flex flex-col justify-start items-center h-40 m-6 w-16 max-h-20 overflow-y-scroll"onClick={() => handleDownloadFile(currentState)}>
            <svg className="flex-shrink-0 h-9 w-9 fill-current text-white "xmlns="http://www.w3.org/2000/svg"height="1em"viewBox="0 0 384 512"><path d="M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128z" /></svg>
            <p className=" w-fit text-white text-xs break-all overflow-y-scroll mt-2 h-6 flex">{currentState.name}</p>
        </button>
        ) : (
        <div className="flex flex-col h-full">
            <div className="flex items-center justify-between px-4 py-2 bg-gray-800 text-white">
            {!isClicked?<button className="px-3 py-1 text-xl font-medium hover:bg-blue-600 text-white rounded"onClick={setClicked}>{currentState.name}</button>:<input className=" text-slate-950" onKeyDown={handleKeyDown} type="text" value={rename} onChange={handleRenameFile}></input>}
                <div className="flex flex-row">
                    <button className="px-3 py-1 text-sm font-medium bg-blue-500 hover:bg-blue-600 text-white rounded"parentFolderItem={currentState} onClick={handleSaveFile}>Save</button>
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