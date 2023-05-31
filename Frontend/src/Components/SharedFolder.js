import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { MyGroups } from './MyGroups';
import { AddUserToGroup} from './AddUserToGroup';
import { GroupAndFolder } from './GroupAndFolder';



export const SharedFolder = () => {
  const [currentGroup,setCurrentGroup] = useState([]);
  const [currentFolders,setCurrentFolders] = useState([]);
  const [count, setCount] = useState(0);
  const [count2,setCount2] = useState(0);
  const [parent,setParent] = useState(0);
  const [currentURL, setCurrentURL] = useState("share");
  const [path,setPath] = useState("/");
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
  const serverToken = Cookies.get('Token');
  useEffect(() => {
    const handlePopstate = () => {
      console.log("handlePopstate");
      console.log(currentGroup.name);
      console.log(currentGroup);
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
        console.log(request);
        fetch('http://localhost:8080/api/groups/get/group/folder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(request)
        }).then((response) => response.json()).then((data) => {
            setCurrentFolders(data);
        }).catch((error) => {
            console.error('Error retrieving data:', error);
        });
          setCurrentURL(currentID);
        }else {
          console.log("else");
          console.log(currentID);
          console.log("-----------------------\n");
          setCurrentURL(currentID);
          const folderRequest ={
            parent:currentID,
            groupID:currentGroup.group_id,
            token:serverToken
          }
          fetch('http://localhost:8080/api/groups/get/group/folder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(folderRequest)
        }).then((response) => response.json()).then((data) => {
            setCurrentFolders(data);
            console.log("data from response");
            console.log(data);
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
    console.log("AddFolder");
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
        console.log(data);
    }).catch((error) => {
    console.error('Error retrieving data:', error);
    });
  }

  const handleChildValue = (data,item) => {
    console.log("handleChildValue");
    console.log(item);
    console.log(data);
    console.log("-----------------------\n");
    setCurrentGroup(item);
    setCurrentURL(item.name);
    setParent(item.group_id);
    setCurrentFolders(data);
  };

  const OpenFolder = (item) =>{
    console.log("OpenFolder in SharedFolders");
    console.log(item);
    navigate(`/share/${currentGroup.name}/${item.folder_id}`);
    const folderRequest ={
      parent:item.folder_id,
      groupID:currentGroup.group_id,
      token:serverToken
    }
    console.log("folderRequest");
    console.log(folderRequest);
    fetch('http://localhost:8080/api/groups/get/group/folder', {
      method: 'POST',
      headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer '+serverToken
      },
      body: JSON.stringify(folderRequest)
  }).then((response) => response.json()).then((data) => {
    console.log(data);
      setCurrentFolders(data);
      setParent(item.folder_id);
      setPath(item.path);
      setCount2(count2+1);
  }).catch((error) => {
      console.error('Error retrieving data:', error);
  });
  console.log("-----------------------\n");

  }
  return (
    <div className=' h-screen bg-slate-500 flex items-start'>
      <div className='bg-orange-600 m-10 flex flex-row'>
            <div>{(currentURL==="share")?(<MyGroups sendDataToParent={handleChildValue}></MyGroups>):(
              <><form className=" bg-orange-300 flex flex-col w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
              <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })} />
              <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
            </form><AddUserToGroup value={currentGroup}></AddUserToGroup></>
            )}
            </div>
            <div className=' flex justify-start mt-10 w-2/4 flex-wrap'>
              {currentURL!=="share"?currentFolders.map((item, index) => (
              <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)} >
                <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>        
                <div className='h-5 w-9' key={index}>
                {item.name}
                </div>
              </button>
              )):[]}
            </div>
      </div>
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