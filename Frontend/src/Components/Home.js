import { useState } from 'react';
import Cookies from 'js-cookie';

export const Home = () => {
  const [data, setData] = useState(null);
  const serverToken = Cookies.get('Token');
  if(serverToken === undefined){
    console.error("Token is undefined");
    return;
  }
  const createGroup = async () => {
    const data2send = { groupname: "group2", token:serverToken};
    console.log(data2send);
    try {
      const response = await fetch('http://localhost:8080/api/groups/create/group', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer '+serverToken
        },
        body: JSON.stringify(data2send)
      });
      const responseData = await response.json();
      setData(responseData);
    } catch (error) {
      console.log("There went something wrong in Home!")
    }
  }

  const getAllGroups = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/groups/get/all', {
        headers: {
          'Authorization': 'Bearer '+serverToken,
        },
      });
      const json = await response.json();
      setData(json);
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <div className=' h-screen bg-slate-500 flex justify-center items-center'>
      <button className=' m-16' onClick={getAllGroups}>get</button>
      {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
      <button onClick={createGroup}>create</button>
      {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
    </div>
  );
}