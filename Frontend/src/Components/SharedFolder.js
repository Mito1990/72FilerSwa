import { useState,useEffect } from 'react';
import Cookies from 'js-cookie';
import {useNavigate } from "react-router-dom"
import {useForm} from "react-hook-form"
import { MyGroups } from './MyGroups';
import { AddUserToGroup} from './AddUserToGroup';
import { CreateNewFile } from './CreateNewFile';
import{OpenFile}from './OpenFile';
import { DeleteUserFromGroup } from './DeleteUserFromGroup';
import { MapItem } from './MapItem';

export const SharedFolder = () => {
  const [currentGroup,setCurrentGroup] = useState();
  const [sharedFolders,setSharedFolders] = useState([]);
  const [isGroupOpen,setIsGroupOpen] = useState(false);
  const [parentID,setParentID] = useState(0);
  const [URL, setURL] = useState("share");
  const [update, setUpdate] = useState(0);
  const {register, handleSubmit} = useForm();
  const navigate = useNavigate();
  const serverToken = Cookies.get('Token');

  useEffect(() => {
    const handlePopstate = async () => {
      let currentURL = getCurrentUrl();
      if(currentURL==='undefined'){
        navigate(`/share`)
        currentURL="/share";
        setURL("share")
        return;
      }else if(currentURL === "share") {
        setURL(currentURL);
      }else if (currentURL === currentGroup.groupName) {
      currentURL = currentGroup.memberGroupID;
      } else  {
        setURL(currentURL);
      }
      try {
        const getFolderRequest = {
          token:serverToken,
          parentID:currentURL
        }
        const response = await fetch('http://localhost:8080/api/folder/getFolder', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+serverToken
            },
            body: JSON.stringify(getFolderRequest)
        });
        const getFolderResponse = await response.json();
        console.error("shareFolderResponse")
        console.error(getFolderResponse)
        setSharedFolders(getFolderResponse)
        } catch (error) {
        console.error('Error retrieving shareFolder:', error);
        }
    };
    window.addEventListener('popstate', handlePopstate);
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, []);


  const AddFolder = async (e) =>{
    console.error("AddFOlder")
    const createNewFolder = {
        token:serverToken,
        folderName:e.NewFolder,
        parentFolderID:parentID,
        isShared:true,
    }
    console.error(createNewFolder)
    await fetch('http://localhost:8080/api/folder/createNewFolder', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+serverToken
    },
    body: JSON.stringify(createNewFolder)
    }).then((response) => response.json()).then((createNewFolderResponse) => {
        setSharedFolders(createNewFolderResponse)
    }).catch((error) => {
    console.error('Error retrieving data:', error);
    });
  }

  const dataFromMyGroups = async(group) => {
    // console.error("sharedFolder")
    // console.error(sharedFolder)
    // console.error("group")
    // console.error(group)
    // setURL(group.groupName);
    // setParentID(group.memberGroupID);
    setCurrentGroup(group);
    setUpdate(update+1)
    // setIsGroupOpen(true);
    // setSharedFolders(sharedFolder)
    // navigate(`/share/${group.groupName}`);
  };

  const handleCreateFile = (data) =>{
    console.error("handleCreateFile -> ")
    console.error(data)
    const folderRequest ={
      parent:parentID,
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
  }).catch((error) => {
      console.error('Error retrieving data:', error);
  });
  }

  // const OpenSharedFolder = async (item) => {
  //   setParentID(item.id);
  //   setIsGroupOpen(false);
  //   setSharedFolders(item);
  //   navigate(`/share/${currentGroup.groupName}/${item.id}`);
  //   const getFolderRequest = {
  //     token:serverToken,
  //     parentID:item.id
  // }
  //   try {
  //     const response = await fetch('http://localhost:8080/api/folder/getFolder', {
  //       method: 'POST',
  //       headers: {
  //         'Content-Type': 'application/json',
  //         'Authorization': 'Bearer ' + serverToken
  //       },
  //       body: JSON.stringify(getFolderRequest)
  //     });
  //     const getFolderRespone= await response.json();
  //   } catch (error) {
  //     console.error('Error retrieving data:', error);
  //   }
  // };
  // const getUpdate = async (data) => {
  //   console.error("hello from getUpdate from shared")
  //   console.log(data);
  // };
const dataFromMapItem = async(currentURL) =>{
  console.log("dataFromMapItem")
  setURL(currentURL)
  console.log(currentURL)
}
  const homeFolder = () =>{
    navigate("/home");
  }
  return (
    <div className='h-screen w-screen bg-slate-500 flex flex-row'>
      <div name="Panel" className='bg-green-300 flex flex-row w-full'>
            <div className='flex flex-col'>
                {(URL==="share") ? (<div className='flex bg-pink-950 flex-row'><MyGroups dataFromMyGroups={dataFromMyGroups}></MyGroups></div>):(
                      <div className='flex flex-col'>
                          <form className=" bg-orange-300 flex flex-col w-52 h-32  justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
                            <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })} />
                            <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
                          </form>
                          <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={homeFolder}>Home</button>
                          <div className=' flex flex-col justify-between bg-white w-52'>
                            <div className='w-52 p-2 bg-emerald-200 flex justify-center items-center'><AddUserToGroup currentGroup={currentGroup}></AddUserToGroup></div>
                            <div className='w-52 p-2 bg-emerald-200 flex justify-center items-center'><DeleteUserFromGroup currentGroup={currentGroup}></DeleteUserFromGroup></div>
                            <div className='w-52 p-2 bg-emerald-200 flex justify-center items-center'><CreateNewFile handleCreateFile={{ handleCreateFile }} currentFolder={sharedFolders} currentGroup={currentGroup}></CreateNewFile></div>
                          </div>
                      </div>
                )}
            </div>
            {/* <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={homeFolder}>Home</button> */}
            <div className=' bg-pink-300 w-full'><MapItem dataFromMapItem={dataFromMapItem} map={update}></MapItem></div>
      </div>
    </div>
  );
}

const getCurrentUrl = () => {
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

// return (
//   <div className=' h-screen w-screen bg-slate-500 flex-row'>
//     <div className='flex flex-row w-full'>
//       <div>{(URL==="share") ? (<MyGroups dataFromMyGroups={dataFromMyGroups}></MyGroups>) : (
//           <><form className=" bg-orange-300 flex flex-col mt-2 ml-2 w-52 h-32 justify-center items-center rounded-md shadow-2xl" onSubmit={handleSubmit(AddFolder)}>
//             <input className="mb-2" type="text" placeholder="New Folder:" name="NewFOlder" {...register('NewFolder', { required: true })} />
//             <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" type="submit">Submit</button>
//           </form>
//           {isGroupOpen ? (
//             <div className='ml-2 flex flex-col'>
//                 <AddUserToGroup currentGroup={currentGroup}></AddUserToGroup>
//                 <DeleteUserFromGroup currentGroup={currentGroup}></DeleteUserFromGroup>
//                 <div className='ml-2'><CreateNewFile handleCreateFile={{handleCreateFile}} currentFolder={sharedFolders} currentGroup={currentGroup}></CreateNewFile></div>
//             </div>
//           ) : ( [] )} </>
//         )}
//       </div>
//       <div className=' flex justify-start mt-2 flex-wrap'>
//         {URL!=="share"?sharedFolders.children.map((sharedFolder, index) => (
//           (!sharedFolder.isFile)?(
//               <button key={index} className=' bg-pink-300 flex flex-row justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenSharedFolder(sharedFolder)} >
//                 <svg className=' h-9 w-9 bg-slate-500' xmlns="http://www.w3.org /2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>
//                 <div className='h-5 w-9' key={index}>
//                 {sharedFolder.name}
//                 </div>
//               </button>
//             ):(
//                 <div className='w-full h-full'key={index}><OpenFile getUpdate={{getUpdate}} parentFolderItem={sharedFolder} groupId={currentGroup.group_id} onDataToParent={[sharedFolder,currentGroup.group_id]} ></OpenFile></div>
//               )
//             )):[]}
//       </div>
//     </div>
//     <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={homeFolder}>Home</button>

//   </div>
// );