import React, { useState } from "react";
import Link from "next/link";
import { Button, Col, Row } from "antd";
import styled from "styled-components";
// import {
//   BsFillSunFill,
//   TbMoonFilled,
//   BsSearch,
//   FaRegUserCircle,
//   SlNote,
// } from "react-icons/all";

const dummy = {
  nickname: "Kyle",
  Post: [],
  isLoggedIn: false,
};

const Wrapper = styled.div`
  height: 4rem;
  display: flex;
  align-items: center;
  justify-content: space-between; /*자식 엘리먼트 사이의 여백을 최대로 설정*/
  .logo {
    font-size: 1.125rem;
    font-weight: 800;
    letter-spacing: 2px;
  }
  .menu {
    display: flex;
    align-items: center;
  }
`;

const Header = ({ children }) => {
  const [isDark, setDark] = useState(true);
  return (
    <Row>
      <Col span={18} offset={3}>
        <Wrapper>
          <div className="logo">
            <Link href="/" style={{ color: "black", textDecoration: "none" }}>
              F12
            </Link>
          </div>

          <div className="Menu">
            <Button>
              {isDark ? (
                <div>어두움</div>
              ) : (
                // <BsFillSunFill />
                <div>밝음</div>
                // <TbMoonFilled
                //   style={{ color: "white", backgroundColor: "black" }}
                // />
              )}
            </Button>
            <Button>
              {<Link href="/search">검색{/*<BsSearch />*/}</Link>}
            </Button>
            {dummy.isLoggedIn ? (
              <>
                <Button>
                  <Link href="/write">
                    작성
                    {/*<SlNote />*/}
                  </Link>
                </Button>
                <Button>
                  <Link href="/profile">프로필{/*<FaRegUserCircle />*/}</Link>
                </Button>
              </>
            ) : (
              <Button>
                <Link href="/login">로그인</Link>
              </Button>
            )}
          </div>
        </Wrapper>
      </Col>
    </Row>
  );
};

export default Header;
