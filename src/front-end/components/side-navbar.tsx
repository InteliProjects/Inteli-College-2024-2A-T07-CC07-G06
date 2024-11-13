"use client";

import { useState, useEffect } from "react";
import {
  ChevronRight,
  ChevronLeft,
  Menu,
  ShoppingBag,
  FileChartPie,
  Upload,
} from "lucide-react";
import { Button } from "./ui/button";
import { Nav } from "./ui/nav";
import Image from "next/image";
import logo from "@/public/vivo_logo.png";
import {
  Drawer,
  DrawerContent,
  DrawerHeader,
  DrawerTrigger,
} from "@/components/ui/drawer";

/**
 * SideNavbar Component
 * 
 * Exibe um menu lateral que se adapta ao tamanho da tela. Em dispositivos maiores,
 * exibe uma barra lateral fixa com opções de navegação. Em dispositivos móveis,
 * exibe um menu deslizante acessível por um botão.
 * 
 * @component
 * @example
 * return <SideNavbar />;
 */
export default function SideNavbar() {
  /**
   * Estado para controlar o colapso da barra lateral.
   * @type {boolean}
   */
  const [isCollapsed, setIsCollapsed] = useState(false);

  /**
   * Estado para determinar se a tela é de tamanho móvel.
   * @type {boolean}
   */
  const [mobileWidth, setMobileWidth] = useState(false);

  /**
   * Hook para atualizar o estado de mobileWidth e isCollapsed
   * com base no tamanho da tela. Adiciona e remove um listener
   * de redimensionamento da janela.
   */
  useEffect(() => {
    const handleResize = () => {
      setMobileWidth(window.innerWidth < 768);
      setIsCollapsed(window.innerWidth < 768);
    };

    handleResize();
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  /**
   * Hook para definir mobileWidth com base na largura da tela.
   */
  useEffect(() => {
    setMobileWidth(window.innerWidth < 768);
  }, []);

  /**
   * Lista de links de navegação.
   * @type {Array<{title: string, href: string, icon: React.ComponentType, variant: string}>}
   */
  const navLinks = [
    {
      title: "E-Commerce",
      href: "/ecommerce",
      icon: ShoppingBag,
      variant: "ghost",
    },
    {
      title: "Testes",
      href: "/testes",
      icon: FileChartPie,
      variant: "ghost",
    },
    {
      title: "Upload CSV",
      href: "/upload",
      icon: Upload,
      variant: "ghost",
    },
  ];

  return (
    <>
      {!mobileWidth && (
        <div className="sticky top-0 h-screen min-w-[80px] border-r flex flex-col">
          <div className="absolute right-[-20px] top-7">
            <Button
              onClick={() => setIsCollapsed(!isCollapsed)}
              variant="secondary"
              className="rounded-full p-2"
            >
              {isCollapsed ? <ChevronRight /> : <ChevronLeft />}
            </Button>
          </div>
          <div className="flex flex-col items-center pt-8 px-3">
            <Image className="pl-1.5 w-20 h-auto" src={logo} alt="Logo" />
          </div>
          <div className="flex-1 px-3 mt-4">
            <Nav
              isCollapsed={isCollapsed}
              links={navLinks.map((link) => ({
                ...link,
                variant: link.variant as "ghost" | "default",
              }))}
            />
          </div>
        </div>
      )}
      {mobileWidth && (
        <div className="fixed w-full flex items-center justify-between px-4 py-2 bg-white border-b top-0 z-50">
          <Drawer>
            <DrawerTrigger asChild>
              <Button>
                <Menu />
              </Button>
            </DrawerTrigger>
            <DrawerContent className="fixed inset-y-0 left-0 w-64">
              <DrawerHeader>
                <div className="flex justify-center">
                  <Image className="pl-1.5 w-24 h-auto" src={logo} alt="Logo" />
                </div>
              </DrawerHeader>
              <div className="px-4 py-2">
                {navLinks.map((link) => (
                  <a key={link.href} href={link.href} className="block mb-2">
                    <Button
                      variant="ghost"
                      className="w-full flex items-center gap-2"
                    >
                      <link.icon className="w-4 h-4" />
                      {link.title}
                    </Button>
                  </a>
                ))}
              </div>
            </DrawerContent>
          </Drawer>
          <Image src={logo} alt="Logo" width={30} height={30} />
        </div>
      )}
    </>
  );
}
