import React, { useState } from 'react';
import Cookies from 'js-cookie';

export const RightClickPopUp = ({dataFromRightClickPopUp,sendCurrentFolderToRightClickPopUp}) => {
const [isOpen, setIsOpen] = useState(true);
const [isRenameButtonClicked, setIsRenameButtonClicked] = useState(false);
const [rename, setRename] = useState('');
const serverToken = Cookies.get('Token');

console.error("hello from RightcliukcPopup top ")
if(sendCurrentFolderToRightClickPopUp.memberGroupID){
    console.error(sendCurrentFolderToRightClickPopUp)
}else if(sendCurrentFolderToRightClickPopUp.id){
    console.error(sendCurrentFolderToRightClickPopUp)

}

const handleOpenPopup = () => {
    setIsOpen(true);
};

const handleClosePopup = () => {
    setIsOpen(false);
    dataFromRightClickPopUp.dataFromRightClickPopUp()
};
const handleDeleteFolder = () => {
    setIsOpen(false);
    dataFromRightClickPopUp.dataFromRightClickPopUp()
};
const handleRenameFolder = () => {
    // setIsOpen(false);
    setIsRenameButtonClicked(true)
    // dataFromRightClickPopUp.dataFromRightClickPopUp()
};
const handleRename = (event) => {
    setRename(event.target.value);
    console.error(event.target.value)
};

const handleKeyDown = async (e) => {
    console.error("hello from handleKeyDown")
    console.error(rename);
    if (e.keyCode === 13) {
        if(sendCurrentFolderToRightClickPopUp.memberGroupID){
            try{
                const renameRequest ={
                    token:serverToken,
                    rename:rename,
                    id:sendCurrentFolderToRightClickPopUp.memberGroupID
                }
                const response = await fetch('http://localhost:8080/api/folder/file/rename/group', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+serverToken
                    },
                    body: JSON.stringify(renameRequest)
                })
                const data = await response.json();
                dataFromRightClickPopUp.dataFromRightClickPopUp();
            }catch(error){
                console.error('Error retrieving data:', error);
            };
            setIsRenameButtonClicked(false);
            setIsOpen(false);
        }
        else{
            try{
                const renameRequest ={
                    token:serverToken,
                    rename:rename,
                    id:sendCurrentFolderToRightClickPopUp.id
                }
                const response = await fetch('http://localhost:8080/api/folder/file/rename/fileElement', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+serverToken
                    },
                    body: JSON.stringify(renameRequest)
                })
                const data = await response.json();
                dataFromRightClickPopUp.dataFromRightClickPopUp();
            }catch(error){
                console.error('Error retrieving data:', error);
            };
            setIsRenameButtonClicked(false);
            setIsOpen(false);
        }
     
    }
}
return (
    <div className="relative">
    {isOpen && (
        <div className="fixed top-0 left-0 right-0 bottom-0 bg-black bg-opacity-50 flex justify-center items-center">
        <div className="bg-white rounded p-4 flex flex-col">
            <button onClick={handleClosePopup} className=" m-2 text-black-500 hover:text-gray-800 flex justify-end">
            <svg className="h-5 w-5 " viewBox="0 0 24 24">
                <path fill="currentColor" d="M6.293 6.293a.999.999 0 0 1 1.414 0L12 10.586l4.293-4.293a.999.999 0 1 1 1.414 1.414L13.414 12l4.293 4.293a.999.999 0 1 1-1.414 1.414L12 13.414l-4.293 4.293a.999.999 0 1 1-1.414-1.414L10.586 12 6.293 7.707a.999.999 0 0 1 0-1.414z" />
            </svg>
            </button>
            <div className='flex flex-col p-1'>
                {isRenameButtonClicked ? (<input className='w-32 h-fit p-2 my-2 rounded-md border-2 placeholder-slate-600'value={rename} onChange={handleRename} onKeyDown={handleKeyDown} placeholder='Rename:' type='text'></input>):<button onClick={handleRenameFolder} className='w-full h-fit p-2 my-2 rounded-md hover:bg-red-600 bg-slate-500'>Rename Folder</button>}
                <button onClick={handleDeleteFolder} className=' w-full h-fit p-2 my-2 rounded-md hover:bg-red-600 bg-slate-500'>Delete Folder</button>
            </div>
            {/* Add your desired content here */}
        </div>
        </div>
    )}
    </div>
);
};

