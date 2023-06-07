import { useState } from "react";
import { useForm } from "react-hook-form";
import Cookies from 'js-cookie';
import { checkPathForHomeOrShare, getCurrentUrl} from "./GetCurrentURL"

export const CreateNewFile = ({dataFromCreateNewFile, currentState}) => {
const { register, handleSubmit, getValues } = useForm();
const [isOpen, setIsOpen] = useState(false);
const serverToken = Cookies.get('Token');
const newFile = () => {setIsOpen(true);}
const handleClose = () => {setIsOpen(false);};
const onSubmit = () => {
    const filename = getValues('File');
    if(checkPathForHomeOrShare()==="share"){
        const newFileRequest = {
            token:serverToken,
            fileName:filename,
            parentFolderID:getCurrentUrl(),
            isShared:true,
        }
        fetch('http://localhost:8080/api/folder/createNewFile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + serverToken
            },
            body: JSON.stringify(newFileRequest)
        }).then((response) => response.json()).then((newFileResponse) => {
            dataFromCreateNewFile.dataFromCreateNewFile(newFileResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }else{
        const filename = getValues('File');
        const newFileRequest = {
            token:serverToken,
            fileName:filename,
            parentFolderID:getCurrentUrl(),
            isShared:false,
        }
        fetch('http://localhost:8080/api/folder/createNewFile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + serverToken
            },
            body: JSON.stringify(newFileRequest)
        }).then((response) => response.json()).then((newFileResponse) => {
            dataFromCreateNewFile.dataFromCreateNewFile(newFileResponse);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
    }
    setIsOpen(false);
}
return (
<div className="flex w-full justify-center mb-2">
    <button onClick={newFile} className= "shadow-slate-800 text-xs shadow-sm w-3/4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
    New File
    </button>
    {isOpen && (
    <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded p-4">
        <button onClick={handleClose} className="text-gray-500 hover:text-gray-700  m-2">
            X
        </button>
        <form className=" bg-orange-300 flex flex-col w-52 h-48 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(onSubmit)}>
            <input className="mb-2" type="text" placeholder="File:" name="username" {...register('File', { required: true })} />
            <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
        </form>
        </div>
    </div>
    )}
</div>
);
}
