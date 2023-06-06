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
import { getCurrentUrl} from "./GetCurrentURL"

export const SharedFolder = () => {
  const [currentGroup,setCurrentGroup] = useState();
  const [parentFolder,setParentFolder] = useState();
  const [URL, setURL] = useState("share");
  const [updateMapItem, setUpdateMapItem] = useState(0);
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
        setUpdateMapItem(updateMapItem+1)
        return;
      }else if(currentURL === "share") {
        setUpdateMapItem(updateMapItem+1)
        setURL(currentURL);
        return;
      }
      else  {
        setURL(currentURL);
      }
        setUpdateMapItem(updateMapItem+1)
    };
    window.addEventListener('popstate', handlePopstate);
    return () => {
      window.removeEventListener('popstate', handlePopstate);
    };
  }, []);


  const AddFolder = async (e) =>{
    const createNewFolder = {
        token:serverToken,
        folderName:e.NewFolder,
        parentFolderID:parentFolder.id,
        isShared:true,
    }
    await fetch('http://localhost:8080/api/folder/createNewFolder', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+serverToken
    },
    body: JSON.stringify(createNewFolder)
    }).then((response) => response.json()).then((createNewFolderResponse) => {
      setUpdateMapItem(updateMapItem+1)
    }).catch((error) => {
    console.error('Error retrieving data:', error);
    });
  }

  const dataFromMyGroups = async(group) => {
    setUpdateMapItem(updateMapItem+1)
  };

  const dataFromCreateNewFile = (data) =>{
    console.error("hello from dataFromCreateNewFile")
    console.error(data)
    setUpdateMapItem(updateMapItem+1)
  }

const dataFromMapItem = async(currentURL,parentFolder,group) =>{
  setURL(currentURL)
  setParentFolder(parentFolder);
  setCurrentGroup(group);
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
                            <div className='w-52 p-2 bg-emerald-200 flex justify-center items-center'><CreateNewFile dataFromCreateNewFile={{ dataFromCreateNewFile }} currentGroup={currentGroup} ></CreateNewFile></div>
                          </div>
                      </div>
                )}
            </div>
            {/* <button className="hover:bg-sky-700 w-24 h-12 border-slate-950 border-2 rounded-xl" onClick={homeFolder}>Home</button> */}
            <div className=' bg-pink-300 w-full'><MapItem dataFromMapItem={{dataFromMapItem}} updateFromMyGroup={{updateMapItem}}></MapItem></div>
      </div>
    </div>
  );
}



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