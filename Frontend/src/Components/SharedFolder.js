import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { MyGroups } from './MyGroups';
import { AddUserToGroup} from './AddUserToGroup';
import { CreateNewFile } from './CreateNewFile';
import{OpenFile}from './OpenFile';



export const SharedFolder = () => {
  const [currentGroup,setCurrentGroup] = useState([]);
  const [currentFolders,setCurrentFolders] = useState([]);
  const [currentFolder,setCurrentFolder] = useState([]);
  const [groupOpen,setGroupOpen] = useState(false);
  const [parent,setParent] = useState(0);
  const [currentURL, setCurrentURL] = useState("share");
  const [path,setPath] = useState("");
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
  const serverToken = Cookies.get('Token');

  useEffect(() => {
    const handlePopstate = () => {
      let currentID = getCurrentFolderIdFromUrl();
      if(currentID==="share"){
        currentID = "share";
        setCurrentURL(currentID);
      }else if(currentID===currentGroup.name){
        const request = {
          groupID:currentGroup.group_id,
          parent:currentGroup.group_id,
          token:serverToken
        }
        setGroupOpen(true);
        fetch('http://localhost:8080/api/groups/get/group/folder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(request)
        }).then((response) => response.json()).then((data) => {
            setCurrentFolders(data);
            setParent(currentGroup.group_id);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
          setCurrentURL(currentID);
        }else {
          setCurrentURL(currentID);
          const folderRequest ={
            parent:currentID,
            groupID:currentGroup.group_id,
            token:serverToken
          }
          console.error(folderRequest);
          fetch('http://localhost:8080/api/groups/get/group/folder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(folderRequest)
        }).then((response) => response.json()).then((data) => {
            setCurrentFolders(data);
            setPath(data.path);
            setParent(currentID);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
        }
    };
       // Attach the handlePopstate function to the popstate event
    window.addEventListener('popstate', handlePopstate);
    // Clean up the event listener on component unmount
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, [currentGroup]);

  const AddFolder = (e) =>{
    const folder = {
        token:serverToken,
        groupID:currentGroup.group_id,
        name:e.NewFolder,
        parent:parent,
        path:path+"/"+e.NewFolder,
        shared:true
    }
    fetch('http://localhost:8080/api/groups/add/folder', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+serverToken
    },
    body: JSON.stringify(folder)
    }).then((response) => response.json()).then((data) => {
        setCurrentFolders(data);
        setPath(path+"/"+e.NewFolder)
    }).catch((error) => {
    console.error('Error retrieving data:', error);
    });
  }

  const handleChildValue = (data,item) => {
    setCurrentGroup(item);
    setCurrentURL(item.name);
    setParent(item.group_id);
    setCurrentFolders(data);
    setGroupOpen(true);
  };
  const handleCreateFile = (data) =>{
    const folderRequest ={
      parent:parent,
      groupID:currentGroup.group_id,
      token:serverToken
    }
    console.log(folderRequest);
    fetch('http://localhost:8080/api/groups/get/group/folder', {
      method: 'POST',
      headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+serverToken
      },
      body: JSON.stringify(folderRequest)
  }).then((response) => response.json()).then((data) => {
      setCurrentFolders(data);
  }).catch((error) => {
      console.error('Error retrieving data:', error);
  });
  }

  const OpenFolder = async (item) => {
    setCurrentFolder(item);
    setParent(item.folder_id);
    setPath(item.path);
    setGroupOpen(false);
    setCurrentFolder(item);
    navigate(`/share/${currentGroup.name}/${item.folder_id}`);
    const folderRequest = {
      parent: item.folder_id,
      groupID: currentGroup.group_id,
      token: serverToken
    };
    try {
      const response = await fetch('http://localhost:8080/api/groups/get/group/folder', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + serverToken
        },
        body: JSON.stringify(folderRequest)
      });
      const data = await response.json();
      setCurrentFolders(data);
    } catch (error) {
      console.error('Error retrieving data:', error);
    }
  };
  
  useEffect( ()  => {
    // Handle the updated currentFolder state here
    console.error('Updated currentFolder:', currentFolder);
    // Perform any necessary actions with the updated state

    // Clean up the effect if needed
    return () => {
      // Cleanup code
    };
  }, [currentFolder]);
  const getUpdate = async (data) => {
    setCurrentFolders(data);
  };

  const homeFolder = () =>{
    navigate("/home");
  }
  return (
    <div className=' h-screen w-screen bg-slate-500 flex-row'>
      <div className='flex flex-row w-full'>
        <div>{(currentURL==="share")?(<MyGroups sendDataToParent={handleChildValue}></MyGroups>):(
          <><form className=" bg-orange-300 flex flex-col mt-2 ml-2 w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
            <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })} />
            <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
          </form>
        {groupOpen?<div className='ml-2'><AddUserToGroup value={currentGroup}></AddUserToGroup></div>:<div className='ml-2'><CreateNewFile handleCreateFile={{handleCreateFile}} currentFolder={currentFolder} currentGroup={currentGroup}></CreateNewFile></div>}</>
        )}
        </div>
        <div className=' flex justify-start mt-2 flex-wrap'>
          {currentURL!=="share"?currentFolders.map((item, index) => (
            (!item.file)?(
                <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)} >
                  <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>
                  <div className='h-5 w-9' key={index}>
                  {item.name}
                  </div>
                </button>
              ):(
                  <OpenFile up={{getUpdate}} onDataToParent={[item,currentGroup.group_id]}></OpenFile>
                )
              )):[]}
        </div>
      </div>
      <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={homeFolder}>Home</button>

    </div>
  );
}

const getCurrentFolderIdFromUrl = () => {
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