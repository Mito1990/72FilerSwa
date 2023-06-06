import { useEffect, useState } from "react";
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import { OpenFile } from "./OpenFile";
import { getCurrentUrl} from "./GetCurrentURL"

export const MapItem = ({dataFromMapItem,updateFromMyGroup}) =>{
    const[list,setList]=useState([]);
    const[isGroup,setIsGroup]=useState(true);
    const[updateFromOpenFile,setUpdateFromOpenFile]=useState(0);
    const[currentGroup,setCurrentGroup]=useState();
    const navigate = useNavigate();
    const serverToken = Cookies.get('Token');
    useEffect(()=>{
    if(getCurrentUrl()==='share'){
        setIsGroup(true)
        const getFolderRequest = {
            token:serverToken,
        }
        fetch('http://localhost:8080/api/memberGroupController/getListOfMemberGroups', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
                'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(getFolderRequest)
            }).then((response) => response.json()).then((memberGroups) => {
                setList(memberGroups)
            }).catch((error) => {
                console.error('Error retrieving shareFolder:', error);
            });
    }else {
        const getFolderRequest = {
            token:serverToken,
            parentID:getCurrentUrl()
        }
        fetch('http://localhost:8080/api/folder/getFolder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
        },body: JSON.stringify(getFolderRequest)
        }).then((response) => response.json()).then((getFolderResponse) => {
            setList(getFolderResponse.children)
        }).catch((error) => {
            console.error('Error retrieving shareFolder:', error);
        });
    }
},[updateFromMyGroup,updateFromOpenFile])

const OpenGroup = async (group) => {
    setList(group.shareFolder.children)
    setCurrentGroup(group)
    navigate(`/share/${group.groupName}/${group.shareFolder.id}`);
    setIsGroup(false);
    const getFolderRequest = {
        token:serverToken,
        parentID:group.shareFolder.id
    }
    try {
        const response = await fetch('http://localhost:8080/api/folder/getFolder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + serverToken
        },
        body: JSON.stringify(getFolderRequest)
        });
        const getFolderResponse= await response.json();
        dataFromMapItem.dataFromMapItem(getCurrentUrl(),getFolderResponse,group)
    } catch (error) {
        console.error('Error retrieving data:', error);
    }
};


const OpenFolder = async (folder) => {
    setList(folder.children)
    setIsGroup(false);
    navigate(`/share/${currentGroup.groupName}/${folder.id}`);
    const getFolderRequest = {
        token:serverToken,
        parentID:folder.id
    }
    try {
        const response = await fetch('http://localhost:8080/api/folder/getFolder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + serverToken
        },
        body: JSON.stringify(getFolderRequest)
        });
        const getFolderResponse= await response.json();
        setList(getFolderResponse.children)
        dataFromMapItem.dataFromMapItem(getCurrentUrl(),getFolderResponse)
    } catch (error) {
        console.error('Error retrieving data:', error);
    }
};
const dataFromOpenFile = async () =>{
    console.error("hello from dataFromOpenFIle")
    setUpdateFromOpenFile(updateFromOpenFile+1);
}
return (
    <div className='flex flex-row flex-wrap'>
        { list.map((elementOfList,index)=>(
            (isGroup ? (
                <div className=''>
                    <button key={index} onClick={() => OpenGroup(elementOfList)} className='flex flex-col justify-items-center m-6' >
                        <svg className="h-9 w-9" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512"><path d="M96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM0 482.3C0 383.8 79.8 304 178.3 304h91.4C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7H29.7C13.3 512 0 498.7 0 482.3zM609.3 512H471.4c5.4-9.4 8.6-20.3 8.6-32v-8c0-60.7-27.1-115.2-69.8-151.8c2.4-.1 4.7-.2 7.1-.2h61.4C567.8 320 640 392.2 640 481.3c0 17-13.8 30.7-30.7 30.7zM432 256c-31 0-59-12.6-79.3-32.9C372.4 196.5 384 163.6 384 128c0-26.8-6.6-52.1-18.3-74.3C384.3 40.1 407.2 32 432 32c61.9 0 112 50.1 112 112s-50.1 112-112 112z"/></svg>
                        <div className='h-5 w-9' key={index}>{elementOfList.groupName}</div>
                    </button>
                </div>
            ):(
                elementOfList.isFile?<div><OpenFile currentFile={elementOfList} currentGroup={currentGroup} dataFromOpenFile={{dataFromOpenFile}}></OpenFile></div>:
                <button key={index} onClick={() => OpenFolder(elementOfList)} className='flex flex-col justify-items-center m-6'  >
                    <svg className=' h-9 w-9' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>
                    <div className='h-5 w-9' key={index}>{elementOfList.name}</div>
                </button>
            )
        )))}
    </div>
);
}

