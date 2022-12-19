import styled from "styled-components";
import colors from "../../colors";

export const Container = styled.div`
  // min-height: 90vh;
  padding: 20px;
  /* Tablet */
  @media only screen and (max-width: 768px) {
    flex-direction: column;
    overflow-y: scroll;
  }
`;

export const Header = styled.div`
border: 1px solid rgb(224, 232, 243); 
padding: 12px;  
margin: 24px 0px; 
display: flex;
justify-content: space-between; 
& .title{font-size: 24px; font-weight: 700; }
  .table-actions { margin-top: 16px; display: flex; justify-content: space-between; align-items: center;
    @media only screen and (max-width: 788px) {
      flex-direction: column;
       gap: 16px;
    }
  }
  .show-page {outline: none; border-radius: 3px; border: 1px solid #b5bdc2; padding: 9px 0px; height: fit-content;}
  & .push-notif-div{background-color: ${colors.blueColor}; border-radius: 4px; padding: 10px; cursor:pointer;
  & .category-btn{cursor:pointer;background-color: ${colors.blueColor};border:none;color:${colors.white};font-size: 15px;}
  & a{color: white;text-decoration: none;}
}  `;
export const Wrapper = styled.div`
  width: 100%;
  background-color: white;
  padding: 2%;
  border-radius: 10px;
  height: fit-content;
  overflow-x:auto;
  /* TABLET */
  @media only screen and (max-width: 768px) {
    width: 100%;
    padding: 30px;
  }

  /* MOBILE */
  @media only screen and (max-width: 460px) {
    padding: 10px;
  }
`;
export const ExportButton = styled.button`
  background-color: ${colors.green6};
  border:none;
  color: white;
  border-radius: 5px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  cursor: pointer;
  font-size: 14px;
`;

export const ActionSection = styled.div`
 display:flex;
 justify-content:space-evenly;
`;
